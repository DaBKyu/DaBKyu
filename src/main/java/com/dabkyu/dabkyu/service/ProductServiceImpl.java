package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ProductOptionDTO;
import com.dabkyu.dabkyu.dto.RelatedProductDTO;
import com.dabkyu.dabkyu.dto.ReportDTO;
import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;
import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;
import com.dabkyu.dabkyu.entity.repository.Category1Repository;
import com.dabkyu.dabkyu.entity.repository.Category2Repository;
import com.dabkyu.dabkyu.entity.repository.Category3Repository;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.entity.RelatedProductEntity;
import com.dabkyu.dabkyu.entity.repository.ProductFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductInfoFileRepository;
import com.dabkyu.dabkyu.entity.repository.ProductOptionRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.RelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.ReportRepository;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ReportRepository reportRepository;
	private final Category1Repository category1Repository;
	private final Category2Repository category2Repository;
	private final Category3Repository category3Repository;
	private final ProductFileRepository productFileRepository;
	private final ProductInfoFileRepository productInfoFileRepository;
	private final ProductOptionRepository productOptionRepository;
	private final RelatedProductRepository relatedProductRepository;

	////////////내가만든거/////////////
	// seachAll에서 사용할 전체 상품 보기
	@Override
	public Page<ProductEntity> productAllList(int pageNum, int postNum, String keyword) throws Exception{
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
		return productRepository.findByProductNameContaining(keyword, pageRequest);
	}

	//상품 이미지 보기////
	public List<ProductFileEntity> productFileList(Long productSeqno) throws Exception{
		return productFileRepository.findByProductSeqno_ProductSeqno(productSeqno);
	 
	}
	/////////////////////////////////
	
	//제품 설명 첨부 파일 보기
	public List<ProductInfoFileEntity> productInfoFileList(Long productSeqno) throws Exception{
		return productInfoFileRepository.findByProductSeqno_ProductSeqno(productSeqno);
	}

	//전체 카테고리1 목록 보기
	@Override
	public List<Category1Entity> category1List() throws Exception {
		return category1Repository.findAll();
	}

	//전체 카테고리2 목록 보기
	@Override
	public List<Category2Entity> category2List() throws Exception {
		return category2Repository.findAll();
	}

	//전체 카테고리3 목록 보기
	@Override
	public List<Category3Entity> category3List() throws Exception {
		return category3Repository.findAll();
	}

	// 전체 상품 보기
	public List<ProductEntity> productList() throws Exception {
		return productRepository.findAll();
	}



	//상품 목록 보기(카테고리 조회)
	@Override
	public Page<ProductEntity> list(int pageNum, int postNum, String keyword, Long cateSeqno) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
		 // 대분류, 중분류, 소분류 순으로 필터링
		 //if (category1Seqno != null) {
            // 대분류 카테고리가 선택된 경우
           // return productRepository.findByCategory1SeqnoAndProductNameContaining(category1Seqno, keyword, pageRequest);
        //} else if (category2Seqno != null) {
            // 중분류 카테고리가 선택된 경우
            //return productRepository.findByCategory2SeqnoAndProductNameContaining(category2Seqno, keyword, pageRequest);
        //} else if (category3Seqno != null) {
		if (cateSeqno != null) {
            // 소분류 카테고리가 선택된 경우
            return productRepository.findByCategory3Seqno_Category3SeqnoAndProductNameContaining(cateSeqno, keyword, pageRequest);
        } else {
            // 카테고리가 선택되지 않은 경우 전체 상품 목록 조회
            return productRepository.findByProductNameContaining(keyword, pageRequest);
   		}		
}

	// //카테고리별 상품 목록 보기
	// @Override
	// public Page<ProductEntity> categoryProduct(int pageNum, int postNum, Long cateSeqno, String keyword) throws Exception {
	// 	// 정렬 기본 순서를 무엇으로 할지 고민 필요: ex. 판매량, 조회, 가격, 찜 ...
	// 	PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
	// 	return productRepository.findByCategory3Seqno_Category3Seqno(cateSeqno, pageRequest);

	// }

  	// 상품 상세 보기
	@Override
	public ProductDTO view(Long productSeqno) throws Exception {
		return productRepository.findById(productSeqno).map(view -> new ProductDTO(view)).get();
	}

	//상품 옵션 보기
	@Override
	public List<ProductOptionDTO> getProductOptions(Long productSeqno) {
    List<ProductOptionEntity> options = productOptionRepository.findByProductSeqno_ProductSeqno(productSeqno);
    return options.stream()
            .map(option -> new ProductOptionDTO(option))
            .collect(Collectors.toList());
	}

	//상품 추가상품 보기
	@Override
	public List<RelatedProductDTO> getRelatedProducts(Long productSeqno) {
		List<RelatedProductEntity> relatedProducts = relatedProductRepository.findByProductSeqno_ProductSeqno(productSeqno);
		return relatedProducts.stream()
				.map(relatedProduct -> new RelatedProductDTO(relatedProduct))
				.collect(Collectors.toList());
	}

	// 상품 이전 보기
	@Override
	public Long pre_seqno(Long productSeqno,String keyword) throws Exception {
		return productRepository.pre_seqno(productSeqno, keyword)==null?0:productRepository.pre_seqno(productSeqno, keyword);	
	}

	// 상품 다음 보기
	@Override
	public Long next_seqno(Long productSeqno,String keyword) throws Exception {
		return productRepository.next_seqno(productSeqno, keyword)==null?0:productRepository.next_seqno(productSeqno, keyword);
	}

	// 상품 첨부파일 목록 보기
	@Override
	public List<ProductFileDTO> fileListView(Long productSeqno) throws Exception {
		//throw new UnsupportedOperationException("Unimplemented method 'fileListView'");
		List<ProductFileDTO> productFileDTOs = new ArrayList<>();
		productFileRepository.findByProductSeqno_ProductSeqno(productSeqno).stream().forEach(list-> productFileDTOs.add(new ProductFileDTO(list)));
		return productFileDTOs;
	}

	// 리뷰 신고
	@Override
	public void report(ReportDTO report) throws Exception {
		report.setReportDate(LocalDateTime.now());
		reportRepository.save(report.dtoToEntity(report));
	}

	
}
