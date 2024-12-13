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
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;

public interface ProductService {
	////////////내가만든거/////////////
	// seachAll에서 사용할 전체 상품 보기
	public Page<ProductEntity> productAllList(int pageNum, int postNum, String keyword) throws Exception;

	// main 썸네일 리스트
	public List<ProductFileEntity> productFileMainList(List<ProductEntity> productList);

	// 썸네일 주소 가져오기
	public String getProductThumbnail(ProductEntity productEntity);

	//상품 이미지 보기////
	public List<ProductFileEntity> productFileList(Long productSeqno) throws Exception;
	
	/////////////////////////////////
	
	//제품 설명 첨부 파일 보기
	public List<ProductInfoFileEntity> productInfoFileList(Long productSeqno) throws Exception;

	//전체 카테고리1 목록 보기
	public List<Category1Entity> category1List() throws Exception;

	//전체 카테고리2 목록 보기
	public List<Category2Entity> category2List() throws Exception;

	//전체 카테고리3 목록 보기
	public List<Category3Entity> category3List() throws Exception;

	// 전체 상품 보기
	public List<ProductEntity> productList() throws Exception;

    //상품 목록 보기
	public Page<ProductEntity> list(int pageNum, int postNum, String keyword, Long cateSeqno) throws Exception;

	// 카테고리별 상품 목록 보기
	// public Page<ProductEntity> categoryProduct(int pageNum, int postNum, Long cateSeqno, String keyword) throws Exception;

	//가장 많이 팔린 상품 10개 조회
	public List<ProductEntity> getTop10BestSellingProducts() throws Exception;

	//로그인한 사용자의 연령대별 가장 많이 팔린 상품 10개 조회
	public List<ProductEntity> getTopProductsByAgeForUser(String email) throws Exception;

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

	public List<ProductEntity> getAllProducts();
}
