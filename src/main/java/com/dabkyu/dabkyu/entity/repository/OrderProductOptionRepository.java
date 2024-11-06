package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntityID;

public interface OrderProductOptionRepository extends JpaRepository<OrderProductOptionEntity, OrderProductOptionEntityID> {

    @Query("Select opo from orderproductoption opo where orderProductSeqno=:orderProductSeqno")
    public List<OrderProductOptionEntity> findByOrderProductSeqno(@Param("orderProductSeqno") Long orderProductSeqno);
}
