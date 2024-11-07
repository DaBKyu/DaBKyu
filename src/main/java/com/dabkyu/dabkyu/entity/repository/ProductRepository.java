package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    public Page<ProductEntity> findByProductSeqnoOrProductNameContaining
		(Long keyword1,String keyword2,Pageable pageable);
    

}