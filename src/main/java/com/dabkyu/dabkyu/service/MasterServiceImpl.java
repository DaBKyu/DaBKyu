package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntityID;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberNotificationEntity;
import com.dabkyu.dabkyu.entity.NotificationEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.repository.CouponCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.CouponTargetRepository;
import com.dabkyu.dabkyu.entity.repository.MasterRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberNotificationRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.NotificationRepository;
import com.dabkyu.dabkyu.entity.repository.OrderDetailRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.ReviewRepository;

import jakarta.transaction.Transactional;
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
    private final NotificationRepository notificationRepository;
    private final MemberNotificationRepository memberNotificationRepository;

    //맴버 리스트 보기
    @Override
    public Page<MemberEntity> memberList(int pageNum, int postNum, String keyword) throws Exception {
        PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"username"));
        return masterRepository.findByEmailContainingOrUsernameContaining(keyword, keyword, pageRequest);
    }

    //맴버 상세보기





    //맴버 이메일로 검색
    public MemberEntity getMemberByEmail(String email){
        return masterRepository.findByEmail(email);
    }
   
    //맴버 등급 수정
    public void saveMemberGrade(MemberEntity memberEntity) {
        masterRepository.save(memberEntity);  
    }
    


    //맴버 삭제 
    @Override
    public void clientDelete(String email) throws Exception{
        //MemberEntity memberEntity = masterRepository.findByEmail(email).get();
        //masterRepository.delete(memberEntity);
    }

    //////////상품

    //상품 리스트 보기 //productEntity,DTO에 productname 없음 
    //상품 _ + 옵션+추가상품도 출력
    //수정필요
    //productList

    //상품삭제
    @Override
    public void deleteProductList(Long productSeqno) throws Exception{
        ProductEntity productEntity = productRepository.findById(productSeqno).get();
        productRepository.delete(productEntity);
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
                //수정어어어엉어어어어어엉어엉
                //file.setProductSeqno((Long)data.get("productSeqno"));
                productFileRepository.save(file);
            }
        
        }
    }

    /////////주문
    //주문 리스트
    @Override
    public void orderList() throws Exception{

    }



    //////////쿠폰
    //쿠폰 리스트 ???????????? 
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
                grade = "Platinum";
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

    // 결제취소 및 환불 신청 내역 조회
    @Override
    public List<OrderDetailEntity> getCancelAndRefundDetails() {
        // '결제취소신청' 또는 '환불신청'인 주문 상태를 가진 주문 조회
        List<String> statuses = List.of("결제취소신청", "환불신청");
        List<OrderInfoEntity> orders = orderInfoRepository.findByOrderStatusIn(statuses);

        // 각 주문에 대해 orderDetail 리스트 반환
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        for (OrderInfoEntity order : orders) {
            orderDetails.addAll(orderDetailRepository.findByOrderSeqno(order));
        }

        return orderDetails;
    }

    // 결제 취소 처리 및 환불 처리(isRefund가 true면 환불, false면 결제취소 처리)
    @Override
    @Transactional
    public void cancelOrRefundOrder(Long orderDetailSeqno, Long couponSeqno, int point, boolean isRefund) {
        OrderDetailEntity orderDetail = orderDetailRepository.findById(orderDetailSeqno).get();

        // 주문 상세의 orderSeqno를 사용해서 OrderInfoEntity 조회
        OrderInfoEntity orderInfo = orderInfoRepository.findByOrderSeqno(orderDetail.getOrderSeqno());

        // 결제 취소 또는 환불에 따른 주문 상태 변경
        String statusMessage = isRefund ? "환불 완료" : "결제 취소 완료";
        if (orderInfo.getOrderStatus().equals(statusMessage)) {
            throw new RuntimeException("이미 " + statusMessage + "된 주문입니다.");
        }
        orderInfo.setOrderStatus(statusMessage);
        orderInfoRepository.save(orderInfo);

        // 주문 상품 목록 조회
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderSeqno(orderInfo);

        // 주문 상세 정보 처리 
        for (OrderDetailEntity orderDetailEntity : orderDetails) {

            if (isRefund) {
                orderDetailEntity.setRefundYn("Y");
            } else {
                orderDetailEntity.setCancelYn("Y");
            }
            orderDetailRepository.save(orderDetailEntity);

            // 주문 상품 정보
            OrderProductEntity orderProduct = orderDetailEntity.getOrderProductSeqno();

            // 주문된 상품의 재고 복구
            ProductEntity product = orderProduct.getProductSeqno();
            product.setStockAmount(product.getStockAmount() + orderProduct.getAmount());
            productRepository.save(product);
        } 

        // 회원정보 불러오기
        MemberEntity member = memberRepository.findById(orderInfo.getEmail().getEmail()).get();

        // 처리완료 후 회원의 누적 구매 금액에서 해당 주문 금액 차감처리
        int totalPrice = orderInfo.getTotalPrice();
        member.setTotalPvalue(member.getTotalPvalue() - totalPrice);

        // 사용한 포인트 및 쿠폰 복구 처리
        // 포인트 복구 처리
        if (point != 0) {
            member.setPoint(member.getPoint() + point); // 환불시 포인트 복구
        }

        // 쿠폰 복원 처리 (만료처리된 쿠폰 복원)
        if (couponSeqno != null) {
            //쿠폰 상태 업데이트 (사용 가능 상태로 변경)
            //MemberCouponEntity memberCoupon = memberCouponRepository.findByCouponSeqno_CouponSeqno(couponSeqno);
            CouponEntity coupon = couponRepository.findByCouponSeqno(couponSeqno);
            coupon.setIsExpire("N");
        }

        memberRepository.save(member);  

        //회원 알림 설정
        String notificationName = isRefund ? "환불 완료" : "결제 취소 완료";
        NotificationEntity notification = NotificationEntity.builder()
                                                            .notificationName(notificationName)  
                                                            .notificationDate(LocalDateTime.now())  
                                                            .build();
    
        // 알림 저장
        notificationRepository.save(notification);

        // 회원에게 알릴 알림 저장
        MemberNotificationEntity memberNotification = MemberNotificationEntity.builder()
                                                                              .email(member)
                                                                              .notificationSeqno(notification)
                                                                              .build();
        memberNotificationRepository.save(memberNotification);

    }

    //관리자가 쿠폰종료일이 지난 쿠폰들 24시에 isExpired를 "Y"로 업데이트 구현필요

}

    
