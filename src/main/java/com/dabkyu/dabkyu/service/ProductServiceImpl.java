package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.ProductDTO;
import com.dabkyu.dabkyu.dto.ProductFileDTO;
import com.dabkyu.dabkyu.dto.ReportDTO;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.ReportRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ReportRepository reportRepository;

	// 상품 목록 보기
	@Override
	public Page<ProductEntity> list(int pageNum, int postNum, String keyword, Long category1Seqno, Long category2Seqno, Long category3Seqno) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
		 // 대분류, 중분류, 소분류 순으로 필터링
		 if (category1Seqno != null) {
            // 대분류 카테고리가 선택된 경우
            return productRepository.findByCategory1SeqnoAndProductNameContaining(category1Seqno, keyword, pageRequest);
        } else if (category2Seqno != null) {
            // 중분류 카테고리가 선택된 경우
            return productRepository.findByCategory2SeqnoAndProductNameContaining(category2Seqno, keyword, pageRequest);
        } else if (category3Seqno != null) {
            // 소분류 카테고리가 선택된 경우
            return productRepository.findByCategory3SeqnoAndProductNameContaining(category3Seqno, keyword, pageRequest);
        } else {
            // 카테고리가 선택되지 않은 경우 전체 상품 목록 조회
            return productRepository.findByProductNameContaining(keyword, pageRequest);
        }
    }

  // 상품 상세 보기
	@Override
	public ProductDTO view(Long productSeqno) throws Exception {
		return productRepository.findById(productSeqno).map(view -> new ProductDTO(view)).get();
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
		throw new UnsupportedOperationException("Unimplemented method 'fileListView'");
	}

	// 리뷰 신고
	@Override
	public void report(ReportDTO report) throws Exception {
		report.setReportDate(LocalDateTime.now());
		reportRepository.save(report.dtoToEntity(report));
	}


}
