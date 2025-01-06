package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.MemberAddressDTO;
import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberAddressEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

public interface MemberService {

    // 회원 가입
    public void signup(MemberDTO member);

    // 회원 정보 수정
	public void modifyMemberInfo(MemberDTO member);

	// 회원 주문제품 내역
	public Page<OrderDetailEntity> orderDetailList(String email, int page, int orderNum, String keyword);

	// 취소, 환불, 교환 내역 조회

	// 배송지 목록 조회
	public List<MemberAddressEntity> addressList(String email);

	/// 특정 배송지 조회
	public MemberAddressDTO viewAddr(Long memberAddressSeqno);
	
	// 기본배송지 조회
	public MemberAddressDTO viewBasicAddr(String email);

	// 배송지 등록
	public void addAddress(MemberAddressDTO address);

	// 배송지 수정
	public void modifyAddress(MemberAddressDTO address);

	// 배송지 삭제
	public void deleteAddress(Long seqno);

	// 내 관심 카테고리 조회
	public List<Category3Entity> myCategoryList(String email);

	// 내 찜한 상품 조회
	public Page<ProductEntity> myLikeList(String email, int page, int productNum);
	
	// 내 리뷰 조회
	public Page<ReviewEntity> myReviewList(String email, int page, int reviewNum);

	// 내 문의 조회
	public Page<QuestionEntity> myQuestionList(String email, int page, int questionNum);

	// 내 쿠폰 조회
	public Page<CouponEntity> myCouponList(String email, int page, int couponNum);
	
	// 패스워드 변경
	public void modifyMemberPassword(MemberDTO member);
	
	// 30일 이후 패스워드 변경 연기
	public void modifyPasswordAfter30(String email);
	
	// 아이디 찾기
	public String searchID(MemberDTO member);
	
	// 아이디 중복 체크
	public int idCheck(String email);
	
	// 회원 정보
	public MemberDTO memberInfo(String email);
	
	// 회원 로그인, 로그아웃, 패스워드변경시간 등록(update)
	public void lastdateUpdate(String email, String status);
    
    // 회원이 등록한 문의 첨부파일 삭제
	public List<QuestionFileEntity> getStoredQuestionFilenameList(String email);

    // 회원이 등록한 리뷰 첨부파일 삭제
	public List<ReviewFileEntity> getStoredReviewFilenameList(String email);
	
	// 회원 정보 삭제
	public void deleteID(String email);
}
