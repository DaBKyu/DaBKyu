package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponCategoryEntityID;
import com.dabkyu.dabkyu.entity.CouponEntity;

import jakarta.transaction.Transactional;

public interface CouponCategoryRepository extends JpaRepository<CouponCategoryEntity, CouponCategoryEntityID> {

   //관리자페이지 쿠폰카테고리 불러오기
   public List<CouponCategoryEntity> findByCouponSeqno(CouponEntity coupon);

   //쿠폰 카테고리 삭제
   @Modifying
   @Query("DELETE FROM couponCategory cc WHERE cc.couponSeqno.couponSeqno = :couponSeqno AND cc.category3Seqno.category3Seqno = :category3Seqno")
   public void deleteByCouponSeqnoAndCategory3Seqno(@Param("couponSeqno") Long couponSeqno, 
                                                    @Param("category3Seqno") Long category3Seqno);
                                                    
   //쿠폰 상세 쿠폰카테고리 조회
   @Query("select cc from couponCategory cc where couponSeqno = :coupon")
   public List<CouponCategoryEntity> findDetailByCouponSeqno(CouponEntity coupon);

   // 특정 쿠폰과 관련된 모든 카테고리 삭제
   @Transactional
   @Modifying
   @Query("DELETE FROM couponCategory cc WHERE cc.couponSeqno.couponSeqno = :couponSeqno")
   void deleteByCouponSeqno(@Param("couponSeqno") Long couponSeqno);


}
