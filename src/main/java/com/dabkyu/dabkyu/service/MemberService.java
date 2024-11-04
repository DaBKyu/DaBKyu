package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.entity.AddressEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

public interface MemberService {

    // 회원 가입
    public void signup(MemberDTO member);

    // 회원 정보 수정
	public void modifyMemberInfo(MemberDTO member);
	
	// 패스워드 변경
	public void modifyMemberPassword(MemberDTO member);
	
	// 30일 이후 패스워드 변경 연기
	public void modifyPasswordAfter30(String email);
	
	// 아이디 찾기
	public String searchID(MemberDTO member);
	
	// 아이디 중복 체크
	public int idCheck(String email);

	// 주소 검색
	public Page<AddressEntity> addrSearch(int pageNum, int postNum, String addrSearch);
	
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
