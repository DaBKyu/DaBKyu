package com.dabkyu.dabkyu.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.entity.ProductEntity;

public interface ProductService {
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


}
