package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.dto.QuestionFileDTO;
import com.dabkyu.dabkyu.entity.QuestionEntity;

public interface QuestionService {

    // 문의 내역 보기
	public Page<QuestionEntity> list(int pageNum, int postNum, String keyword) throws Exception;

    // 문의 내용 보기
	public QuestionDTO view(Long queSeqno) throws Exception;

    // 문의 내용 이전 보기
	public Long pre_seqno(Long queSeqno,String keyword) throws Exception;
	
	// 문의 내용 다음 보기
	public Long next_seqno(Long queSeqno,String keyword) throws Exception;

    // 문의 등록 
	public void write(QuestionDTO question) throws Exception;

    // max seqno 구하기
	public Long getMaxSeqno(String email) throws Exception;

    // 첨부파일 정보 등록하기
	public void fileInfoRegistry(QuestionFileDTO questionFile) throws Exception;

    // 첨부파일 목록 보기
	public List<QuestionFileDTO> fileListView(Long queSeqno) throws Exception;

}
