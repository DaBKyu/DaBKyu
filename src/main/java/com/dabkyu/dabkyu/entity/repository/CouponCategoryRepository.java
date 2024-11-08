package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;

public interface CouponCategoryRepository extends JpaRepository<CouponCategoryEntity, Long> {

   //관리자페이지 쿠폰카테고리 불러오기
   public List<CouponCategoryEntity> findByCouponSeqno(CouponEntity coupon);
    
}
