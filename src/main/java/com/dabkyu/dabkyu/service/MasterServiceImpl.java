package com.dabkyu.dabkyu.service;

import java.time.LocalDate;
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
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.repository.CouponCategoryRepository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.CouponTargetRepository;
import com.dabkyu.dabkyu.entity.repository.MasterRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
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
    
