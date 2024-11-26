package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    // 특정 주문에 해당하는 모든 주문 상세 정보 조회
    public List<OrderDetailEntity> findByOrderSeqno_OrderSeqno(OrderInfoEntity orderSeqno);

    // 특정 주문 상품에 해당하는 주문 상세 정보 조회
    public OrderDetailEntity findByOrderSeqno_OrderSeqno(Long orderSeqno);

    //public Page<OrderDetailEntity> findAll(PageRequest pageRequest);

    //주문리스트 카테고리3으로 카테고리+상품명 조회
    /* 
    @Query("SELECT od FROM orderDetail od " +
            "JOIN od.orderProductSeqno op " +
            "JOIN op.productSeqno p " +
            "JOIN p.category3Seqno c3 " +
            "WHERE c3.category3Seqno = :category3Seqno " +
            "AND p.productName LIKE %:productName%")
    public Page<OrderDetailEntity> findByCategoryAndProductNameContaining(@Param("category") Long category, 
                                                               @Param("productName") String productName, Pageable pageable);
    
    //주문리스트 카테고리3으로 카테고리 조회
    @Query("SELECT od FROM orderDetail od " +
            "JOIN od.orderProductSeqno op " +
            "JOIN op.productSeqno p " +
            "JOIN p.category3Seqno c3 " +
            "WHERE c3.category3Seqno = :category3Seqno")
    public Page<OrderDetailEntity> findByCategory(@Param("category") Long category, Pageable pageable);
    */

    //주문리스트 카테고리3으로 카테고리 OR 상품명 조회
    @Query("SELECT od FROM orderDetail od " +
            "JOIN od.orderProductSeqno op " +
            "JOIN op.productSeqno p " +
            "JOIN p.category3Seqno c3 " +
            "WHERE (:category3Seqno IS NULL OR c3.category3Seqno = :category3Seqno) " +
            "AND (:productName = '' OR p.productName LIKE %:productName%)")
    public Page<OrderDetailEntity> findByCategoryAndProductNameContaining(@Param("category") Long category, 
                                                               @Param("productName") String productName, Pageable pageable);

    //주문리스트 상품명으로 조회
    //public Page<OrderDetailEntity> findByProductNameContaining(String productname, Pageable pageable);

    public Page<OrderDetailEntity> findByOrderProductSeqno_ProductSeqno_ProductNameContaining(String productName, Pageable pageable);

}
