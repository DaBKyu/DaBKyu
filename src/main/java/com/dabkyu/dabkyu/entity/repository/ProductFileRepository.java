package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;


public interface ProductFileRepository extends JpaRepository<ProductFileEntity, Long> {

     // 특정 상품과 연관된 첫 번째 이미지를 가져오는 메서드
    public ProductFileEntity findTopByProductSeqnoOrderByProductFileSeqnoAsc(ProductEntity product);

    // 특정 상품에 대한 모든 이미지 파일을 가져오는 메서드
    public List<ProductFileEntity> findByProductSeqno_ProductSeqno(Long productSeqno);

    public ProductFileEntity findFirstByProductSeqnoAndIsThumb(ProductEntity product, String isThumb);
}
