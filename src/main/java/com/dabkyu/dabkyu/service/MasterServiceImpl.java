package com.dabkyu.dabkyu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.OrderProductDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.dto.QuestionFileDTO;
import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.repository.AddedRelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.CouponCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.CouponTargetRepository;
import com.dabkyu.dabkyu.entity.repository.MasterRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.OrderDetailRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;
import com.dabkyu.dabkyu.entity.repository.OrderProductOptionRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.QuestionRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MasterServiceImpl implements MasterService {


    private final MemberRepository memberRepository;
    private final MasterRepository masterRepository;
    private final CouponRepository couponRepository;
    private final CouponCategoryRepository couponCategoryRepository;
    private final CouponTargetRepository couponTargetRepository;
    private final ProductRepository productRepository;
    private final ProductFileRepository productFileRepository;
    private final ReviewRepository reviewRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AddedRelatedProductRepository addedRelatedProductRepository;
    private final OrderProductOptionRepository orderProductOptionRepository;
    private final QuestionRepository questionRepository;
    private final QuestionService questionService;

    //맴버 리스트 보기
    @Override
    public Page<MemberEntity> memberList(int pageNum, int postNum, String keyword) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"username"));
        return masterRepository.findByEmailContainingOrUsernameContaining(keyword, keyword, pageRequest);
    }

    //맴버 이메일로 검색
    public MemberDTO getMemberByEmail(String email){
        return memberRepository.findByEmail(email).map((member)->new MemberDTO(member)).get();
    }
   
    //맴버 등급 수정
    public void memberModify(MemberDTO memberDTO) throws Exception{
        MemberEntity memberEntity = memberRepository.findByEmail(memberDTO.getEmail()).get();
        memberEntity.setEmail(memberDTO.getEmail());
        memberEntity.setUsername(memberDTO.getUsername());
        memberEntity.setTelno(memberDTO.getTelno());
        memberEntity.setGender(memberDTO.getGender());
        memberEntity.setBirthDate(memberDTO.getBirthDate());
        memberEntity.setMemberGrade(memberDTO.getMemberGrade());
        memberEntity.setPay(memberDTO.getPay());
        memberEntity.setPoint(memberDTO.getPoint());
        memberEntity.setRole(memberDTO.getRole());
        memberEntity.setEmailRecept(memberDTO.getEmailRecept());
        masterRepository.save(memberEntity);  
    }

    //맴버 삭제 
    @Override
    public void clientDelete(String email) throws Exception{
        memberRepository.deleteByEmail(email);
    }

    //////////상품
    //상품 리스트 보기 
    @Override
    public Page<ProductDTO> productList(int pageNum, int postNum, Long keyword1, String keyword2) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "productName"));
        Page<ProductEntity> productEntities;

        if (keyword1 != null) {
            //상품이름 + 카테고리
            productEntities = productRepository.findByCategory3Seqno_Category3SeqnoAndProductNameContaining(keyword1, keyword2, pageRequest);
        } else {
            //상품이름
            productEntities = productRepository.findByProductNameContaining(keyword2, pageRequest);
        }

        List<ProductDTO> productDTOs = productEntities.stream().map(productEntity -> {
    
            List<ProductFileEntity> productFiles = productFileRepository.findByProductSeqno_ProductSeqno(productEntity.getProductSeqno());
            ProductDTO productDTO = new ProductDTO(productEntity);
            
            //productfile 정보 -> productDTO에 추가
            productFiles.forEach(file -> {
                ProductFileDTO productFileDTO = new ProductFileDTO(file);
                productDTO.getProductFiles().add(productFileDTO); 
            });    
            return productDTO; 
        }).collect(Collectors.toList());

        return new PageImpl<>(productDTOs, pageRequest, productEntities.getTotalElements());
    }

    //상품등록
    @Override
    public Long productPost(ProductDTO productDTO) throws Exception{
        return productRepository.save(productDTO.dtoToEntity(productDTO)).getProductSeqno();
    }

    //상품수정
    @Override
    public void productModify(ProductDTO productDTO) throws Exception{
        ProductEntity productEntity = productRepository.findById(productDTO.getProductSeqno()).get();
        productEntity.setCategory3Seqno(productDTO.getCategory3Seqno());
        productEntity.setProductName(productDTO.getProductName());
        productEntity.setProductInfo(productDTO.getProductInfo());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setStockAmount(productDTO.getStockAmount());
        productEntity.setDeliveryisFree(productDTO.getDeliveryisFree());
        productEntity.setSecretYn(productDTO.getSecretYn());
        productRepository.save(productEntity);
    }

    //max seqno 구하기
	// @Override
	// public Long getMaxSeqno(Long productSeqno) throws Exception{
	// 	return productRepository.getMaxSeqno(productSeqno);
	// } 

    //상품 이미지 파일 등록
    @Override
    public void productImgFile(ProductFileDTO productFileDTO) throws Exception{
        productFileRepository.save(productFileDTO.dtoToEntity(productFileDTO));
    }

    //상품 이미지 삭제
    @Override
    public void deleteProductFile(Long productFileSeqno) throws Exception{
        //ProductEntity productEntity = productRepository.findById(productFileSeqno).get();
        //productFileRepository.delete(productEntity);
        productFileRepository.deleteById(productFileSeqno);
    }


    //상품 삭제(활성 비활성)
    @Override
    public List<ProductDTO> getAllProducts() throws Exception {
        List<ProductEntity> productEntities = productRepository.findAll(); // 모든 제품 가져오기
        return productEntities.stream()
                            .map(ProductDTO::new)
                            .collect(Collectors.toList());
}

    //상품첨부파일삭제
    @Override
    public void deleteProductFileList(Map<String,Object> data) throws Exception{
        ProductFileEntity productFileEntity = null;
        List<ProductFileEntity> productFileEntities = null;

        if(data.get("kind").equals("F")){
            productFileEntity = productFileRepository.findById((Long)data.get("productfileSeqno")).get();
            productFileRepository.save(productFileEntity);
        }
        if(data.get("kind").equals("B")){
            productFileEntities = productFileRepository.findByProductSeqno_ProductSeqno((Long)data.get("productSeqno"));
            for(ProductFileEntity file:productFileEntities){
                //수정필요
                //file.setProductSeqno((Long)data.get("productSeqno"));
                productFileRepository.save(file);
            }
        
        }
    }

    /////////주문
    //주문 리스트 //검색 > 주문현황으로 가능하게 추가해야함. 
    @Override
    public Page<Map<String, Object>> orderList(int pageNum, int postNum, String productname, Long category) throws Exception{
        
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by("orderDate").descending());
        Page<OrderDetailEntity> orderDetailPage = orderDetailRepository.findAll(pageRequest);

        /*
        검색기능 구현해야함
        if (category != null) {
            orderDetailPage = orderDetailRepository.findByCategoryAndProductNameContaining(category, productname, pageRequest);
        } else {
            orderDetailPage = orderDetailRepository.findByProductNameContaining(productname, pageRequest);
        }
         */

        return orderDetailPage.map(orderDetail -> {
        Map<String, Object> result = new HashMap<>();

        // 주문 정보 
        OrderInfoEntity orderInfoEntity = orderDetail.getOrderSeqno(); 
        OrderInfoDTO orderInfoDTO = new OrderInfoDTO(orderInfoEntity);
        result.put("orderInfo", orderInfoDTO); 

        // 주문 상품 정보 
        OrderProductEntity orderProductEntity = orderDetail.getOrderProductSeqno(); 
        ProductEntity productEntity = orderProductEntity.getProductSeqno();
        result.put("productName", productEntity.getProductName()); 
        result.put("productPrice", productEntity.getPrice());

        // 카테고리 정보 
        String category1Name = orderProductEntity.getProductSeqno().getCategory3Seqno().getCategory2Seqno().getCategory1Seqno().getCategory1Name();
        String category2Name = orderProductEntity.getProductSeqno().getCategory3Seqno().getCategory2Seqno().getCategory2Name();
        String category3Name = orderProductEntity.getProductSeqno().getCategory3Seqno().getCategory3Name();
        result.put("category1Name", category1Name);
        result.put("category2Name", category2Name);
        result.put("category3Name", category3Name);

        // 취소 여부, 환불 여부
        result.put("cancelYn", orderDetail.getCancelYn());
        result.put("refundYn", orderDetail.getRefundYn());

        // 추가 상품
        List<AddedRelatedProductEntity> relatedProducts = addedRelatedProductRepository.findByOrderProductSeqno(orderProductEntity.getOrderProductSeqno());
        if(!relatedProducts.isEmpty()){
            result.put("relatedProducts", relatedProducts);
        }

        // 옵션
        List<OrderProductOptionEntity> productOptions = orderProductOptionRepository.findByOrderProductSeqno(orderProductEntity.getOrderProductSeqno());
        if (productOptions != null && !productOptions.isEmpty()) {
            result.put("productOptions", productOptions); 
        }

        return result; // 최종 결과 반환
        });
    }

    //주문 상태 수정
    @Override
    public void modifyOrderStatus(Long orderSeqno, String newStatus) throws Exception{
        OrderInfoEntity orderInfoEntity = orderInfoRepository.findByOrderSeqno(orderSeqno);
        orderInfoEntity.setOrderStatus(newStatus);
        orderInfoRepository.save(orderInfoEntity);
    }


    //////////쿠폰
    //쿠폰 리스트 
    //!!확인 필요!!
    @Override
    public Page<Map<String,Object>> couponList(int pageNum, int postNum, String keyword) throws Exception{
        
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"couponStartDate"));

        Page<CouponEntity> coupons = couponRepository.findAll(pageRequest);
        //List<CouponCategoryEntity> couponCategories = couponCategoryRepository.findAll();
        //List<CouponTargetEntity> couponTargetsTargets = couponTargetRepository.findAll();
        
        //카테고리, 타겟 집어넣
        Map<CouponEntity, Object> couponDataMap = new HashMap<>();

        for (CouponEntity coupon : coupons) {
            //couponType : T (카테고리로 적용)
            if ("T".equals(coupon.getCouponType())) {
                List<CouponCategoryEntity> categories = couponCategoryRepository.findByCouponSeqno(coupon);
                if (!categories.isEmpty()) {
                    couponDataMap.put(coupon, categories); 
                } else {
                    couponDataMap.put(coupon, new ArrayList<>()); 
                }
            //couponType : C (단일 상품으로 적용) 
            } else if ("C".equals(coupon.getCouponType())) {
                List<CouponTargetEntity> targets = couponTargetRepository.findByCouponSeqno(coupon);
                if (!targets.isEmpty()) {
                    couponDataMap.put(coupon, targets); 
                } else {
                    couponDataMap.put(coupon, new ArrayList<>()); 
                }
            }
        }
        //합치기
        return coupons.map(coupon -> {

            Object couponData = couponDataMap.get(coupon);

            Map<String, Object> result = new HashMap<>();
            result.put("coupon", coupon); //기존 쿠폰 정보      
            result.put("couponData", couponData); //쿠폰 카테고리, 타겟 정보

            return result;
        });
    }
    

    //쿠폰 등록
    //수정필요
    @Override
    public void writeCoupon(CouponDTO coupon) throws Exception{
        couponRepository.save(coupon.dtoToEntity(coupon));
    }

    //쿠폰 수정
    @Override
    public void modifyCoupon(CouponDTO coupon) throws Exception{
        CouponEntity couponEntity = couponRepository.findById(coupon.getCouponSeqno()).get();
        couponEntity.setCouponName(coupon.getCouponName());
        couponEntity.setCouponType(coupon.getCouponType());
        couponEntity.setCouponTarget(coupon.getCouponTarget());
        couponEntity.setCouponInfo(coupon.getCouponInfo());
        couponEntity.setPercentDiscount(coupon.getPercentDiscount());
        couponEntity.setAmountDiscount(coupon.getAmountDiscount());
        couponEntity.setCouponStartDate(coupon.getCouponStartDate());
        couponEntity.setCouponEndDate(coupon.getCouponEndDate());
        couponEntity.setMinOrder(coupon.getMinOrder());
        couponEntity.setCouponRole(coupon.getCouponRole());
        couponRepository.save(couponEntity);
    }

    //쿠폰 삭제
    public void deleteCoupon(Long couponSeqno) throws Exception{
            CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();
            couponRepository.delete(couponEntity);
    }

    /////////////문의
    //문의 리스트
    @Override
    public Page<Map<String, Object>> questionList(int pageNum, int postNum, String queType) throws Exception{
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "queDate"));
        Page<QuestionEntity> questionEntities;

        if (!queType.isEmpty()) {
            questionEntities = questionRepository.findByQueType(queType, pageRequest);
        } else {
            questionEntities = questionRepository.findAll(pageRequest);
        }

        return questionEntities.map(questionEntity -> {
            Map<String, Object> result = new HashMap<>();
            QuestionDTO questionDTO = new QuestionDTO(questionEntity);

            // 문의 정보
            result.put("question", questionDTO);
            
            // 첨부 파일
            List<QuestionFileDTO> questionFiles = new ArrayList<>();
            try {
                questionFiles = questionService.fileListView(questionEntity.getQueSeqno());
            } catch (Exception e) {
                questionFiles = new ArrayList<>();
            }
            if (!questionFiles.isEmpty()) {
                result.put("questionFiles", questionFiles);
            } else {
                result.put("questionFiles", new ArrayList<>()); 
            }
            
            return result;
        });
        
    }


    
    /////////////리뷰
    //리뷰 리스트 
    @Override
    public Page<ReviewEntity> reviewList(int pageNum, int postNum, Long keyword){
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"revDate"));
		return reviewRepository.findByReviewSeqnoOrProductSeqno_ProductSeqno(keyword, keyword, pageRequest);
    }

    //리뷰 삭제
    @Override
	public void deleteReview(Long reviewSeqno) throws Exception{

    }

    //리뷰 첨부파일 삭제
    @Override
    public void deleteReviewFileList(Map<String, Object> data) throws Exception{
        //ReviewFileEntity reviewFileEntity = null;
        //List<ReviewFileEntity> reviewFileEntities = null;

        
    }

    //전체 회원의 누적 구매금액을 조회 후 총 누적 구매금액 업데이트, 
    //최근 3년이내의 누적 구매금액을 기준으로 등급 업데이트
    @Override
    public void calculateAndUpdateCustomerGrade(LocalDateTime referenceDate) {
        List<MemberEntity> members = memberRepository.findAll();

        for (MemberEntity member : members) {
            int totalSpentLast3Years = 0;  // 최근 3년 이내의 누적구매금액
            LocalDateTime threeYearsAgo = referenceDate.minusYears(3); 
            List<OrderInfoEntity> orders = orderInfoRepository.findByEmail_Email(member.getEmail());

            for (OrderInfoEntity order : orders) {
                int orderTotalPrice = order.getTotalPrice();

                // 최근 3년 이내의 누적구매금액 구하기
                if (order.getOrderDate().isAfter(threeYearsAgo)) {
                totalSpentLast3Years += orderTotalPrice;
                }
            }
            // 회원의 총 누적구매금액 업데이트
            member.setTotalPvalue(totalSpentLast3Years);

            //최근 3년 이내의 누적구매 금액에 따라 회원등급 설정
            String grade;
            if (totalSpentLast3Years >= 1000000) {
                grade = "VIP";
            } else if (totalSpentLast3Years >= 500000) {
                grade = "Gold";
            } else if (totalSpentLast3Years >= 100000) {
                grade = "Silver";
            } else {
                grade = "Bronze";
            }

            //회원 객체의 memberGrade 업데이트 및 저장
            member.setMemberGrade(grade);
            //memberRepository.save(member);
        }   
        // 모든 회원 정보 한 번에 저장
        memberRepository.saveAll(members); 
    }
}
    
