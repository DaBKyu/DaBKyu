package com.dabkyu.dabkyu.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.LikeListEntity;
import com.dabkyu.dabkyu.entity.MemberCategoryEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.repository.Category3Repository;
import com.dabkyu.dabkyu.entity.repository.LikeListRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.RecentViewRepository;
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
    private final RecentViewRepository recentViewRepository; 
    private final MemberRepository memberRepository;  
    private final MemberCategoryRepository memberCategoryRepository; // MemberCategoryRepository 추가
    private final Category3Repository category3Repository; 
    private final ProductRepository productRepository;

    // 카테고리 선택 후 해당 카테고리를 관심 카테고리로 지정한 사람들에게 이메일 발송
    @PostMapping("/send/categoryInterestMail")
    public String sendCategoryInterestMail(@RequestParam Long category3Seqno) {
        // 해당 카테고리를 관심 카테고리로 지정한 사용자들 조회
        List<MemberCategoryEntity> memberCategories = memberCategoryRepository.findByCategory3Seqno_Category3Seqno(category3Seqno);

        // 이메일 내용 생성
        String content = createInterestedCategoriesContent(memberCategories);

        // 해당 카테고리를 관심 카테고리로 지정한 사용자들에게 이메일 발송
        for (MemberCategoryEntity memberCategory : memberCategories) {
            MemberEntity member = memberCategory.getEmail();
            emailService.sendMail(new String[]{member.getEmail()}, "관심 카테고리의 최신 상품을 확인해보세요!", content, null);
        }
        return "{\"message\":\"Email sent successfully to interested category members\"}";
    }

    // 찜한 상품 선택 후 해당 상품을 찜한 사람들에게 이메일 발송
    @PostMapping("/send/likedItemMail")
    public String sendLikedItemMail(@RequestParam Long productSeqno) {
        // 해당 상품을 찜한 사용자들 조회
        List<LikeListEntity> likedItems = likeListRepository.findByProductSeqno_ProductSeqno(productSeqno);

        // 이메일 내용 생성
        String content = createLikedItemsContent(likedItems);

        // 해당 상품을 찜한 사용자들에게 이메일 발송
        for (LikeListEntity likeItem : likedItems) {
            MemberEntity member = likeItem.getEmail();
            emailService.sendMail(new String[]{member.getEmail()}, "찜한 상품 할인 소식!", content, null);
        }
        return "{\"message\":\"Email sent successfully to users who liked the item\"}";
    }

    // 관심 카테고리 이메일 내용 생성 (수정된 부분)
    private String createInterestedCategoriesContent(List<MemberCategoryEntity> memberCategories) {
        StringBuilder content = new StringBuilder("관심 있는 카테고리의 최신 상품:\n");

        // 관심 카테고리 목록을 이용해 카테고리 이름을 가져와 이메일 내용 생성
        for (MemberCategoryEntity memberCategory : memberCategories) {
            Category3Entity category = memberCategory.getCategory3Seqno();  // 관심 카테고리 정보
            content.append("- ").append(category.getCategory3Name()).append("\n");

            // 해당 카테고리 내 상품들 조회
            List<ProductEntity> productsInCategory = productRepository.findByCategory3Seqno_Category3Seqno(category.getCategory3Seqno());
            for (ProductEntity product : productsInCategory) {
                content.append("  - ").append(product.getProductName()).append("\n");
            }
        }

        content.append("새로운 상품들을 지금 바로 만나보세요!");
        return content.toString();
    }

    // 찜한 상품 이메일 내용 생성
    private String createLikedItemsContent(List<LikeListEntity> likedItems) {
        StringBuilder content = new StringBuilder("찜한 상품들이 할인 중입니다:\n");

        for (LikeListEntity item : likedItems) {
            ProductEntity product = item.getProductSeqno();  // 찜한 상품 정보
            content.append("- ").append(product.getProductName()).append("\n");
        }
        content.append("지금 바로 확인하세요!");
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

