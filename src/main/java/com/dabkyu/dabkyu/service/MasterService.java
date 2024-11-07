package com.dabkyu.dabkyu.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface MasterService {

    //맴버 리스트
    public Page<MemberEntity> memberList(int pageNum, int postNum, String keyword) throws Exception;

    //
	public MemberDTO viewMember(String email) throws Exception;

    //////////////상품
    //상품 리스트 
    public Page<ProductEntity> productList(int pageNum, int postNum, Long keyword1, String keyword2) throws Exception;
    
    //상품 삭제
    public void deleteProductList(Long productSeqno) throws Exception;

	//상품 첨부파일 삭제
	public void deleteProductFileList(Map<String,Object> data) throws Exception;

    ////////////주문
    //주문 리스트
    


    /////////////쿠폰
    //쿠폰 리스트
    public Page<CouponEntity> couponList(int pageNum, int postNum, String keyword) throws Exception;
    
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


}
