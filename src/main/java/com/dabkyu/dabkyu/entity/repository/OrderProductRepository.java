package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
    
    // 사용자 이메일과 검색어로 주문제품 검색
    // @Query("SELECT op FROM orderProduct op " +
    //               "JOIN op.productSeqno p " +
    //               "JOIN orderDetail od ON od.orderproductSeqno = op " +
    //               "JOIN od.orderSeqno oi " +
    //               "WHERE oi.email.email = :email " +
    //               "AND p.productName LIKE %:keyword%")
    @Query("SELECT od FROM orderDetail od " +
                    "JOIN od.orderProductSeqno op " +
                    "JOIN op.productSeqno p " +
                    "JOIN od.orderSeqno oi " +
                    "WHERE oi.email.email = :email " +
                    "AND p.productName LIKE %:keyword%")
    public Page<OrderProductEntity> findOrderProductsByEmailAndProductNameContaining(
        @Param("email") String email,
        @Param("keyword") String keyword,
        Pageable pageable
    );

    //주문 상품 조회
    //public List<OrderProductEntity> findByOrderSeqno(OrderInfoEntity orderSeqno);

    //public List<OrderProductEntity> findByEmailAndSeqnos(String email, List<Long> orderProductSeqnos);

}