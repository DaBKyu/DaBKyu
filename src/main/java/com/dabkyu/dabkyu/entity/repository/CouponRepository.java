package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.CouponEntity;


public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
        
   public Page<CouponEntity> findByCouponNameContainingOrCouponTypeContaining
            (String keyword1,String keyword2,Pageable pageable);

   //전체 조회..
   public Page<CouponEntity> findAll(Pageable pageable);
   
}
