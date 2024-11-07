package com.dabkyu.dabkyu.entity.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ShoppingCartEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntityID;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, ShoppingCartEntityID> {
        // 이메일을 통해 장바구니 항목 조회
        List<ShoppingCartEntity> findByEmail(String email);

        // 이메일과 OrderProductSeqno를 통해 특정 장바구니 항목 조회
        ShoppingCartEntity findByEmailAndOrderProductSeqno(String email, Long orderProductSeqno);
}
