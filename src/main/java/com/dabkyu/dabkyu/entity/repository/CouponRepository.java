package com.dabkyu.dabkyu.entity.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;


public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
        
   public Page<CouponEntity> findByCouponNameContaingOrCouponTypeContaing
            (String keyword1,String keyword2,Pageable pageable);
    
}
