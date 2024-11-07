package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.AddedRelatedProductEntityID;

public interface AddedRelatedProductRepository extends JpaRepository<AddedRelatedProductEntity, AddedRelatedProductEntityID> {

    @Query("select p from addedRelatedProduct p where p.orderProductSeqno=:orderProductSeqno")
    public List<AddedRelatedProductEntity> findByOrderProductSeqno(@Param("orderProductSeqno") Long orderProductSeqno);

}

