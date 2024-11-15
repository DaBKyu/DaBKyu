package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntityID;
import com.dabkyu.dabkyu.entity.MemberEntity;


public interface MemberCouponRepository extends JpaRepository<MemberCouponEntity,MemberCouponEntityID> {

    // 회원 쿠폰조회(쿠폰 종료일 오름차순 조회)
    @Query("SELECT mc.couponSeqno FROM memberCoupon mc " +
                   "JOIN mc.couponSeqno c " +
                   "WHERE mc.email=:email " +
                   "ORDER BY c.couponEndDate ASC")
    public Page<CouponEntity> findMyCouponByEmail(
        @Param("email") MemberEntity member,
        Pageable pageable
        );

    public MemberCouponEntity findByCouponSeqno_CouponSeqno(Long couponSeqno);
}
