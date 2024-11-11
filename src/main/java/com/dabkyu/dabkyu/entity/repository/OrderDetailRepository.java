package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    // 특정 주문에 해당하는 모든 주문 상세 정보 조회
    public List<OrderDetailEntity> findByOrderSeqno(OrderInfoEntity orderSeqno);

    // 특정 주문 상품에 해당하는 주문 상세 정보 조회
    public List<OrderDetailEntity> findByOrderProductSeqno(OrderProductEntity orderProduct);

    //public OrderDetailEntity findByOrderProductSeqno(OrderProductEntity orderProduct);


}
