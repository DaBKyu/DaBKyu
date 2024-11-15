package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ProductOptionEntity;

public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {

    public List<ProductOptionEntity> findByProductSeqno_ProductSeqno(Long productSeqno);

}
