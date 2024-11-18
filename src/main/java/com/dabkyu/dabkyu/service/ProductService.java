package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ReportDTO;
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.ProductEntity;

public interface ProductService {
	//전체 카테고리1 목록 보기
	public List<Category1Entity> category1List() throws Exception;

	//전체 카테고리2 목록 보기
	public List<Category2Entity> category2List() throws Exception;

	//전체 카테고리3 목록 보기
	public List<Category3Entity> category3List() throws Exception;

    //상품 목록 보기
	public Page<ProductEntity> list(int pageNum, int postNum, String keyword, Long category1Seqno, Long category2Seqno, Long category3Seqno) throws Exception;

    //상품 상세 보기
	public ProductDTO view(Long productSeqno) throws Exception;
	
	//상품 이전 보기
	public Long pre_seqno(Long productSeqno,String keyword) throws Exception;
	
	//상품 다음 보기
	public Long next_seqno(Long productSeqno,String keyword) throws Exception;

	//상품 첨부파일 목록 보기
	public List<ProductFileDTO> fileListView(Long productSeqno) throws Exception;

	// 리뷰 신고
	public void report(ReportDTO report) throws Exception;
}
