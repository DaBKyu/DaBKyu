package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.OrderInfoEntity;

public interface OrderInfoRepository extends JpaRepository<OrderInfoEntity, Long> {
    
    public List<OrderInfoEntity> findByEmail_Email(String email);

    public OrderInfoEntity findByEmail_EmailAndOrderSeqno(String email, Long orderSeqno);

    public Page<OrderInfoEntity> findByOrderSeqnoOrEmail_Email
            (Long keyword1,String keyword2,Pageable pageable);

    public List<OrderInfoEntity> findByOrderStatus(String orderStatus);

}
