package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    // 특정 주문에 해당하는 모든 주문 상세 정보 조회
    public List<OrderDetailEntity> findByOrderSeqno(OrderInfoEntity orderSeqno);

    // 특정 주문 상품에 해당하는 주문 상세 정보 조회
    public OrderDetailEntity findByOrderSeqno_OrderSeqno(Long orderSeqno);

    public Page<OrderDetailEntity> findAll(PageRequest pageRequest);

    @Query("SELECT od FROM OrderDetailEntity od " +
       "JOIN od.orderProductSeqno op " +
       "JOIN op.productSeqno p " +
       "WHERE (p.category3Seqno.category2Seqno.category1Seqno.category1Seqno = :category) " +
       "AND (p.productName LIKE %:productName%)")
    public Page<OrderDetailEntity> findByCategoryAndProductNameContaining(@Param("category") Long category, 
                                                               @Param("productName") String productName);
                                                 
    @Query("SELECT od FROM OrderDetailEntity od " +
           "JOIN od.orderProductSeqno op " +
           "JOIN op.productSeqno p " +
           "WHERE (p.category3Seqno.category2Seqno.category1Seqno.category1Seqno = :category)")
    public Page<OrderDetailEntity> findByCategory(@Param("category") Long category);

    public Page<OrderDetailEntity> findByProductNameContaining(String productname);


}
