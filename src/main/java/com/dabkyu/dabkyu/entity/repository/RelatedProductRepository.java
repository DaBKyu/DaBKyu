package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.dabkyu.dabkyu.entity.RelatedProductEntity;

public interface RelatedProductRepository extends JpaRepository<RelatedProductEntity, Long>{

    public List<RelatedProductEntity> findByProductSeqno_ProductSeqno(Long productSeqno);

}
