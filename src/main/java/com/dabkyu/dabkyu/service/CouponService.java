package com.dabkyu.dabkyu.service;


public interface CouponService {

    //쿠폰 사용가능 여부 체크 및 사용기한 만료 후 30일 이후 삭제 처리
    public void checkCouponStatus(Long couponSeqno, String email) throws Exception;
	
}
