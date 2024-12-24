package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;

public interface ProductInfoFileRepository extends JpaRepository<ProductInfoFileEntity, Long> {

    public List<ProductInfoFileEntity> findByProductSeqno_ProductSeqno(Long productSeqno);

    //상품 상세 이미지 찾기
    @Query("SELECT p FROM productInfoFile p WHERE p.productSeqno = :productSeqno")
    public List<ProductInfoFileEntity> findByProductSeqno(@Param("productSeqno") Long productSeqno);
}
