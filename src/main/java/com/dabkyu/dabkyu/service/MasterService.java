package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface MasterService {

    //맴버 리스트
    public Page<MemberEntity> memberList(int pageNum, int postNum, String keyword) throws Exception;

    //맴버 삭제
    public void clientDelete(String email) throws Exception;


    //맴버 이메일로 검색
    public MemberEntity getMemberByEmail(String email);

    //맴버 등급 수정
    public void saveMemberGrade(MemberEntity memberEntity);


    //////////////상품
    //상품 리스트 
    //productList
    
    //상품 삭제
    public void deleteProductList(Long productSeqno) throws Exception;

	//상품 첨부파일 삭제
	public void deleteProductFileList(Map<String,Object> data) throws Exception;

    ////////////주문
    //주문 리스트
    public void orderList() throws Exception;


    /////////////쿠폰
    //쿠폰 리스트
    public Page<Map<String,Object>> couponList(int pageNum, int postNum, String keyword) throws Exception;
    
    //쿠폰등록
    public void writeCoupon(CouponDTO coupon) throws Exception;
	
	//쿠폰수정
	public void modifyCoupon(CouponDTO coupon) throws Exception;

	//쿠폰삭제
	public void deleteCoupon(Long couponSeqno) throws Exception;

    //////////////리뷰
    //리뷰 리스트
    public Page<ReviewEntity> reviewList(int pageNum, int postNum, Long keyword);

    //리뷰 첨부파일 삭제
    public void deleteReviewFileList(Map<String, Object> data) throws Exception;

    //리뷰 삭제
	public void deleteReview(Long reviewSeqno) throws Exception;

    //전체 회원의 누적구매금액을 조회 후 등급업데이트
    public void calculateAndUpdateCustomerGrade(LocalDateTime referenceDate);

    //결제 취소 및 환불 신청 내역 보기
    public List<OrderDetailEntity> getCancelAndRefundDetails();

    //결제 취소 및 환불 처리
    public void cancelOrRefundOrder(Long orderDetailSeqno, Long couponSeqno, int point, boolean isRefund);

}
