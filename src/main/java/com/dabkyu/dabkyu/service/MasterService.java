package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.dabkyu.dabkyu.dto.CouponDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface MasterService {

    //맴버 리스트
    public Page<MemberEntity> memberList(int pageNum, int postNum, String keyword) throws Exception;

    //맴버 삭제
    public void clientDelete(String email) throws Exception;

    //맴버 이메일로 검색
    public MemberDTO getMemberByEmail(String email);

    //맴버 등급 수정
    public void memberModify(MemberDTO memberDTO) throws Exception;


    //////////////상품
    //상품 리스트 
    public Page<ProductDTO> productList(int pageNum, int postNum, Long keyword1, String keyword2) throws Exception;
    
    //상품등록
    public Long productPost(ProductDTO productDTO) throws Exception;

    //상품수정
    public void productModify(ProductDTO productDTO) throws Exception;

    //max seqno 구하기
	// public Long getMaxSeqno(Long productSeqno) throws Exception;

    //상품 이미지 파일 등록
    public void productImgFile(ProductFileDTO productFileDTO) throws Exception;

    //상품 이미지 삭제
    public void deleteProductFile(Long fileseqno) throws Exception;

    //상품 삭제(활성 비활성)
    public List<ProductDTO> getAllProducts() throws Exception;

	//상품 첨부파일 삭제
	public void deleteProductFileList(Map<String,Object> data) throws Exception;

    ////////////주문
    //주문 리스트
    public Page<Map<String, Object>> orderList(int pageNum, int postNum, String productname, Long category) throws Exception;

    //주문 상태 수정
    public void modifyOrderStatus(Long orderSeqno, String newStatus) throws Exception;

    /////////////쿠폰
    //쿠폰 리스트
    public Page<Map<String,Object>> couponList(int pageNum, int postNum, String keyword) throws Exception;
    
    //쿠폰등록
    public void writeCoupon(CouponDTO coupon) throws Exception;
	
	//쿠폰수정
	public void modifyCoupon(CouponDTO coupon) throws Exception;

	//쿠폰삭제
	public void deleteCoupon(Long couponSeqno) throws Exception;

    /////////////문의
    //문의 리스트
    public Page<Map<String, Object>> questionList(int pageNum, int postNum, String queType) throws Exception;


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
