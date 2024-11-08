package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntityID;

public interface CouponTargetRepository extends JpaRepository<CouponTargetEntity, CouponTargetEntityID> {
    
    //쿠폰 타겟 불러오기
    public List<CouponTargetEntity> findByCouponSeqno(CouponEntity coupon);
    
}
