package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;

public interface ProductInfoFileRepository extends JpaRepository<ProductInfoFileEntity, Long> {

    public List<ProductInfoFileEntity> findByProductSeqno_ProductSeqno(Long productSeqno);
}
