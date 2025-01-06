package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.CouponEntity;


public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
        
   public Page<CouponEntity> findByCouponNameContainingOrCouponTypeContaining
            (String keyword1,String keyword2,Pageable pageable);

   //전체 조회..
   public Page<CouponEntity> findAll(Pageable pageable);

   //만료된 쿠폰들만 조회하는 쿼리 메서드
   @Query("SELECT c FROM coupon c WHERE c.couponEndDate < :referenceDate")
   public List<CouponEntity> findExpiredCoupons(@Param("referenceDate") LocalDateTime referenceDate);

   public List<CouponEntity> findByCouponEndDateBefore(LocalDateTime referenceDate);

   public CouponEntity findByCouponCode(String couponCode);

   
}
