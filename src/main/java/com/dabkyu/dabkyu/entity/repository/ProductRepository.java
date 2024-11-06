package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    //상품 목록 보기
    public Page<ProductEntity> findByProductNameContaining
    (String keyword,Pageable pageable);

    //상품 이전 보기
	@Query("select max(p.productSeqno) from product p " 
			+ "where p.productSeqno < :productSeqno and "
			+ "(p.productName like %:keyword%)")
	public Long pre_seqno(@Param("productSeqno") Long productSeqno, 
			@Param("keyword") String keyword);
	
    //상품 다음 보기
	@Query("select min(p.productSeqno) from product p "
			+ "where p.productSeqno < :productSeqno and "
			+ "(p.productName like %:keyword%)")
	public Long next_seqno(@Param("productSeqno") Long productSeqno, 
			@Param("keyword") String keyword);

}
