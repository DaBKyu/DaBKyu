package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;

public interface CouponTargetRepository extends JpaRepository<CouponTargetEntity, Long> {
    
    //쿠폰 타겟 불러오기
    public List<CouponTargetEntity> findByCouponSeqno(CouponEntity coupon);
    
}
