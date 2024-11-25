package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.dabkyu.dabkyu.dto.EmailDTO;
import com.dabkyu.dabkyu.dto.EmailFileDTO;
import com.dabkyu.dabkyu.entity.EmailEntity;

public interface EmailService {

    // 이메일 발송
    public void sendEmail(String title, String content, 
    MultipartFile[] mailFileList, String kind, 
    List<Long> category3SeqnoList, List<Long> productSeqnoList, List<Long> couponSeqnoList);

    // max seqno 구하기
	public Long getMaxSeqno() throws Exception;

    //관심카테고리저장
    public void saveEmailCategory(Long maxSeqno, List<Long>category3SeqnoList);
    
    //찜상품저장
    public void saveEmailLike(Long maxSeqno, List<Long>productSeqnoList);

    //파일 저장
    public void saveEmailFile(Long maxSeqno, MultipartFile[] mailFileList);

    // 메일 발송 내역 조회
	public Page<EmailEntity> list(int pageNum, int postNum, String keyword) throws Exception;

	//메일 상세 내용 보기
	public EmailDTO view(Long emailSeqno) throws Exception;
	
	//메일 내용 이전 보기
	public Long pre_seqno(Long emailSeqno,String keyword) throws Exception;
	
	//메일 내용 다음 보기
	public Long next_seqno(Long emailSeqno,String keyword) throws Exception;

    //첨부파일 목록 보기
	public List<EmailFileDTO> fileListView(Long emailSeqno) throws Exception;

}
