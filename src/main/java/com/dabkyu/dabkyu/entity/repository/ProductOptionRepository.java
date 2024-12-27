package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ProductOptionEntity;

public interface ProductOptionRepository extends JpaRepository<ProductOptionEntity, Long> {

    public List<ProductOptionEntity> findByProductSeqno_ProductSeqno(Long productSeqno);

    @Query("SELECT po FROM productOption po WHERE po.optionSeqno = :optionSeqno")
    public ProductOptionEntity findByOptionSeqno(@Param("optionSeqno") Long optionSeqno);

}
