package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ProductFileEntity;


public interface ProductFileRepository extends JpaRepository<ProductFileEntity, Long> {
    
    //public List<ProductFileEntity> findByProductFileSeqno_ProductFileSeqno(Long productfileSeqno);
    public List<ProductFileEntity> findByProductSeqno_ProductSeqno(Long productSeqno);
}
