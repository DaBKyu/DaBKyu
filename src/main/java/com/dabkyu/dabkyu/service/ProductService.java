package com.dabkyu.dabkyu.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ProductOptionDTO;
import com.dabkyu.dabkyu.dto.RelatedProductDTO;
import com.dabkyu.dabkyu.dto.ReportDTO;
import com.dabkyu.dabkyu.dto.TopSellingProductDTO;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.service.ProductServiceImpl.TopProduct;

public interface ProductService {

	//상품 목록 보기(메인페이지)
	public Page<ProductEntity> findProductList(int pageNum, int postNum, String keyword) throws Exception;

    //상품 목록 보기(카테고리 조회)
	public Page<ProductEntity> list(int pageNum, int postNum, String keyword, Long category1Seqno, Long category2Seqno, Long category3Seqno) throws Exception;

	//가장 많이 팔린 상품 10개 조회
	public List<TopSellingProductDTO> getTop10BestSellingProducts() throws Exception;

	//로그인한 사용자의 연령대별 가장 많이 팔린 상품 10개 조회 테스트
	public Map<String, List<TopProduct>> getTopProductsByAgeForUser(String email) throws Exception;

	//연령대별 상위 10개 상품 조회
	//public Map<String, List<TopProduct>> getTop10ProductsByAgeGroup() throws Exception;

    //상품 상세 보기
	public ProductDTO view(Long productSeqno) throws Exception;

	//상품 옵션 보기
	public List<ProductOptionDTO> getProductOptions(Long productSeqno);

	//상품 추가상품 보기
	public List<RelatedProductDTO> getRelatedProducts(Long productSeqno);
	
	//상품 이전 보기
	public Long pre_seqno(Long productSeqno,String keyword) throws Exception;
	
	//상품 다음 보기
	public Long next_seqno(Long productSeqno,String keyword) throws Exception;

	//상품 첨부파일 목록 보기
	public List<ProductFileDTO> fileListView(Long productSeqno) throws Exception;

	// 리뷰 신고
	public void report(ReportDTO report) throws Exception;
}
