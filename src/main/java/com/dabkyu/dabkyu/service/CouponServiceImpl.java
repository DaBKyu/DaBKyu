package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService{

    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final MemberCouponRepository memberCouponRepository;

    //쿠폰 사용가능 여부 체크 및 사용기한 만료 후 30일 이후 삭제 처리
    @Override
    public void checkCouponStatus(Long couponSeqno, String email) throws Exception{
        //쿠폰, 회원정보 조회
        CouponEntity couponEntity = couponRepository.findById(couponSeqno).get();
        MemberEntity memberEntity = memberRepository.findById(email).get();

        //특정 회원의 모든 쿠폰 조회
        List<MemberCouponEntity> coupons = memberCouponRepository.findByEmail_Email(email);
        LocalDateTime now = LocalDateTime.now(); //현재 날짜 검색

        for(MemberCouponEntity memberCoupon : coupons){

            //사용가능 표기 쿠폰 
            if("N".equals(memberCoupon.getIsExpire())){

                //사용기한이 만료되었을 경우
                if(couponEntity.getCouponEndDate().isBefore(now)){
                    memberCoupon.setIsExpire("Y");
                }

                //등급(사용조건)이 일치하지 않을 경우
                String userRole = memberEntity.getRole();
                if(!userRole.equals(couponEntity.getCouponRole())){
                    memberCoupon.setIsExpire("Y");
                }
            }

            //기한 만료 후 30일 이후 삭제 처리
            if("Y".equals(memberCoupon.getIsExpire())){ 
                LocalDateTime couponEndDateTime = couponEntity.getCouponEndDate(); //쿠폰 만료일 검색
                long daysBetween = ChronoUnit.DAYS.between(couponEndDateTime, now); //일수 계산

                if(daysBetween > 30){
                    deleteCoupon(couponSeqno);
                }
            }
        }
        memberCouponRepository.saveAll(coupons);
    }

    //쿠폰삭제
    private void deleteCoupon(Long couponSeqno) throws Exception{
        couponRepository.deleteById(couponSeqno);
    }
}
