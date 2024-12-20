package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntityID;
import com.dabkyu.dabkyu.entity.ProductEntity;
import jakarta.transaction.Transactional;

public interface CouponTargetRepository extends JpaRepository<CouponTargetEntity, CouponTargetEntityID> {
    
    //쿠폰 타겟 불러오기
    public List<CouponTargetEntity> findByCouponSeqno(CouponEntity coupon);

    
    //쿠폰 타겟 삭제
    @Modifying
    @Query("DELETE FROM couponTarget ct WHERE ct.couponSeqno.couponSeqno = :couponSeqno AND ct.productSeqno.productSeqno = :productSeqno")
    public void deleteByCouponSeqnoAndProductSeqno(@Param("couponSeqno") Long couponSeqno,
                                                   @Param("productSeqno") Long productSeqno);

    // 쿠폰 상세 타겟 조회
    @Query("SELECT ct FROM couponTarget ct WHERE ct.couponSeqno = :coupon")
    public List<CouponTargetEntity> findDetailByCouponSeqno(@Param("coupon") CouponEntity coupon);
         
}
