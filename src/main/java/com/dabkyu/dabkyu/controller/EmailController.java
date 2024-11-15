package com.dabkyu.dabkyu.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.LikeListEntity;
import com.dabkyu.dabkyu.entity.MemberCategoryEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.repository.LikeListRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.OrderDetailRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.service.EmailService;
import com.dabkyu.dabkyu.service.MailSendService;
import com.dabkyu.dabkyu.util.PageUtil;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@AllArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MailSendService mailSendService;
    private final LikeListRepository likeListRepository;  
    private final MemberCategoryRepository memberCategoryRepository; 
    private final ProductRepository productRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderDetailRepository orderDetailRepository;

    // 관심 카테고리 이메일 발송
    @PostMapping("/send/categoryInterestMail")
    public String sendCategoryInterestMail(@RequestParam List<Long> category3Seqnos,
                                           @RequestParam(required = false) MultipartFile[] attachments) {
        Map<String, List<MemberCategoryEntity>> userInterestCategoryMap = new HashMap<>();

        for (Long category3Seqno : category3Seqnos) {
            List<MemberCategoryEntity> memberCategories = memberCategoryRepository.findByCategory3Seqno_Category3Seqno(category3Seqno);

            for (MemberCategoryEntity memberCategory : memberCategories) {
                MemberEntity member = memberCategory.getEmail();
                if ("Y".equals(member.getEmailRecept())) {  // emailRecept가 "Y"인지 확인
                    String email = member.getEmail();
                    // 최근 이메일 발송 내역 확인

                    

                    userInterestCategoryMap
                        .computeIfAbsent(email, k -> new ArrayList<>())
                        .add(memberCategory);
                }
            }
        }

        for (Map.Entry<String, List<MemberCategoryEntity>> entry : userInterestCategoryMap.entrySet()) {
            String email = entry.getKey();
            List<MemberCategoryEntity> memberCategories = entry.getValue();
            String content = createInterestedCategoriesContent(memberCategories);

            emailService.sendMail(new String[]{email}, "관심 카테고리", content, attachments);
        }

        return "{\"message\":\"Email sent successfully to interested category members\"}";
    }

    // 찜한 상품 이메일 발송
    @PostMapping("/send/likedItemMail")
    public String sendLikedItemMail(@RequestParam List<Long> productSeqnos,
                                    @RequestParam(required = false) MultipartFile[] attachments) {
        Map<String, List<LikeListEntity>> userLikedItemsMap = new HashMap<>();

        for (Long productSeqno : productSeqnos) {
            List<LikeListEntity> likedItems = likeListRepository.findByProductSeqno_ProductSeqno(productSeqno);

            for (LikeListEntity likeItem : likedItems) {
                MemberEntity member = likeItem.getEmail();
                if ("Y".equals(member.getEmailRecept())) {  // emailRecept가 "Y"인지 확인
                    String email = member.getEmail();

                    userLikedItemsMap
                        .computeIfAbsent(email, k -> new ArrayList<>())
                        .add(likeItem);
                }
            }
        }

        for (Map.Entry<String, List<LikeListEntity>> entry : userLikedItemsMap.entrySet()) {
            String email = entry.getKey();
            List<LikeListEntity> likedItems = entry.getValue();
            String content = createLikedItemsContent(likedItems);

            emailService.sendMail(new String[]{email}, "찜한 상품", content, attachments);
        }

        return "{\"message\":\"Email sent successfully to users who liked the items\"}";
    }

    private String createInterestedCategoriesContent(List<MemberCategoryEntity> memberCategories) {
        StringBuilder content = new StringBuilder("관심 있는 카테고리:\n");

        for (MemberCategoryEntity memberCategory : memberCategories) {
            Category3Entity category = memberCategory.getCategory3Seqno();
            content.append("- ").append(category.getCategory3Name()).append("\n");

            List<ProductEntity> productsInCategory = productRepository.findByCategory3Seqno_Category3Seqno(category.getCategory3Seqno());
            for (ProductEntity product : productsInCategory) {
                content.append("  - ").append(product.getProductName()).append("\n");
            }
        }

        content.append("지금 바로 만나보세요!");
        return content.toString();
    }

    private String createLikedItemsContent(List<LikeListEntity> likedItems) {
        StringBuilder content = new StringBuilder("찜한 상품:\n");

        for (LikeListEntity item : likedItems) {
            ProductEntity product = item.getProductSeqno();
            content.append("- ").append(product.getProductName()).append("\n");
        }

        content.append("지금 바로 확인하세요!");
        return content.toString();
    }

    //주문일이 2년 지난 상품 교체주기 알림 이메일 발송
    @PostMapping("/send/productReplacementCycleMail")
    public String sendProductReplacementCycleMail() {
    LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2); // 2년 전 날짜 계산
    
    // 2년 이상 지난 주문들을 조회
    List<OrderInfoEntity> orderInfos = orderInfoRepository.findByOrderDateBefore(twoYearsAgo);
    
    Map<String, List<ProductEntity>> memberProductMap = new HashMap<>();

    //이메일수신날짜emailReceptDate기준으로 발송내역이 최근에 있다면 보내지않기(발송제목으로 구분?) 

    // 주문된 상품들에 대해 처리
    for (OrderInfoEntity orderInfo : orderInfos) {
        MemberEntity member = orderInfo.getEmail(); // 주문한 회원
        if ("Y".equals(member.getEmailRecept())) { // 이메일 수신 동의 여부 확인
            // OrderDetailEntity를 직접 조회해서 상품 정보 처리
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderSeqno_OrderSeqno(orderInfo); // OrderDetailEntity 조회
            for (OrderDetailEntity orderDetail : orderDetails) {
                ProductEntity product = orderDetail.getOrderProductSeqno().getProductSeqno();
                memberProductMap
                    .computeIfAbsent(member.getEmail(), k -> new ArrayList<>())
                    .add(product); // 회원에 해당하는 상품 추가
            }
        }
    }

    // 각 회원에 대해 교체주기 이메일 발송
    for (Map.Entry<String, List<ProductEntity>> entry : memberProductMap.entrySet()) {
        String email = entry.getKey();
        List<ProductEntity> products = entry.getValue();
        String content = createReplacementCycleContent(products); // 교체주기 내용 생성

        // 이메일 발송
        emailService.sendMail(new String[]{email}, "교체주기 상품 안내", content, null);
    }

        return "{\"message\":\"Replacement cycle emails sent successfully.\"}";
    }
    
    // 교체주기 상품에 대한 이메일 내용 생성
    private String createReplacementCycleContent(List<ProductEntity> products) {
        // 이메일 내용 시작 부분
        StringBuilder content = new StringBuilder("안녕하세요! 아래의 상품은 교체주기가 도래했습니다:\n");

        // 상품 목록을 순회하며 내용에 추가
        for (ProductEntity product : products) {
            content.append("- ").append(product.getProductName()).append(" (교체 주기 안내)\n");
        }

        // 이메일 내용 끝 부분
        content.append("\n지금 바로 교체 가능한 상품을 확인해보세요!\n");
        content.append("감사합니다.");
        
        // 이메일 내용 반환
        return content.toString();
    }


  
    // 메일 발송 내역 출력
    @GetMapping("/mailSendList")
    public void getMailSendList(
            Model model,
            @RequestParam("page") int pageNum,
            @RequestParam(name = "category", defaultValue = "", required = false) Long category
    ) {
        // 페이지네이션
        int pageSize = 10;  // 한 페이지에 표시할 메일 발송 내역의 개수
        long totalCount = mailSendService.getTotalMailSendCount();  // 전체 메일 발송 내역 개수
        PageUtil pageUtil = new PageUtil();
        String pageList = pageUtil.getMailSendPageList(pageNum, pageSize, 5, totalCount);

        // 메일 발송 내역 조회
        model.addAttribute("mailSendList", mailSendService.getMailSendList(pageSize, pageNum));
        model.addAttribute("pageList", pageList);
    }
}

