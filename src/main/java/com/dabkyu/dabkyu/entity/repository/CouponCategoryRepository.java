package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponCategoryEntityID;
import com.dabkyu.dabkyu.entity.CouponEntity;

public interface CouponCategoryRepository extends JpaRepository<CouponCategoryEntity, CouponCategoryEntityID> {

   //관리자페이지 쿠폰카테고리 불러오기
   public List<CouponCategoryEntity> findByCouponSeqno(CouponEntity coupon);

   //쿠폰 카테고리 삭제
   @Modifying
   @Query("DELETE FROM couponCategory cc WHERE cc.couponSeqno.couponSeqno = :couponSeqno AND cc.category3Seqno.category3Seqno = :category3Seqno")
   public void deleteByCouponSeqnoAndCategory3Seqno(@Param("couponSeqno") Long couponSeqno, 
                                                    @Param("category3Seqno") Long category3Seqno);
                                                
}
