package com.dabkyu.dabkyu.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.ReviewDTO;
import com.dabkyu.dabkyu.dto.ReviewFileDTO;
import com.dabkyu.dabkyu.entity.MemberReviewLikeEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface ReviewService {

    // 상품 리뷰 보기
	public Page<ReviewEntity> list(int pageNum, int postNum, String keyword) throws Exception;

	//게시물 상세 내용 보기
	public ReviewDTO view(Long reviewSeqno) throws Exception;
	
	//게시물 내용 이전 보기
	public Long pre_seqno(Long reviewSeqno,String keyword) throws Exception;
	
	//게시물 내용 다음 보기
	public Long next_seqno(Long reviewSeqno,String keyword) throws Exception;

    // 상품 리뷰 등록 하기
	public void write(ReviewDTO review) throws Exception;
	
	// max seqno 구하기
	public Long getMaxSeqno(String email) throws Exception;
	
	// 첨부파일 정보 등록하기
	public void fileInfoRegistry(ReviewFileDTO reviewFile) throws Exception;

	//첨부파일 목록 보기
	public List<ReviewFileDTO> fileListView(Long seqno) throws Exception;

	//게시물 삭제하기
	public void delete(Long reviewSeqno) throws Exception;	
	
	//파일 삭제 : B--> 게시물 삭제 시 게시물에 속한 파일 전체 삭제, F --> 수정시 선택한 파일 선택
	public void deleteFileList(Map<String, Object> data) throws Exception;

	//게시물 도움이되었어요 갯수 수정
	public void reviewLikeUpdate(ReviewDTO review) throws Exception;
	
	//도움이되었어요 등록여부 확인
	public MemberReviewLikeEntity likeCheckView(Long reviewSeqno,String email) throws Exception;
	
	//도움이되었어요 신규 등록
	public void likeCheckRegistry(Long reviewSeqno, String email) throws Exception;
	
	//도움이되었어요 취소
	public void likeCheckUpdate(Long reviewSeqno, String email) throws Exception;


}
