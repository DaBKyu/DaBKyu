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

    private final ProductRepository shopRepository;
    private final ReportRepository reportRepository;

    //상품 목록 보기
    @Override
    public Page<ProductEntity> list(int pageNum, int postNum, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"productSeqno"));
		return shopRepository.findByProductNameContaining(keyword, pageRequest);
    }

    //상품 상세 보기
	@Override
	public ProductDTO view(Long productSeqno) throws Exception {
		return shopRepository.findById(productSeqno).map(view -> new ProductDTO(view)).get();
	}
	
	//상품 이전 보기
	@Override
	public Long pre_seqno(Long productSeqno,String keyword) throws Exception {
		return shopRepository.pre_seqno(productSeqno, keyword)==null?0:shopRepository.pre_seqno(productSeqno, keyword);	
	}

	//상품 다음 보기
	@Override
	public Long next_seqno(Long productSeqno,String keyword) throws Exception {
		return shopRepository.next_seqno(productSeqno, keyword)==null?0:shopRepository.next_seqno(productSeqno, keyword);
	}

	//상품 첨부파일 목록 보기
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
