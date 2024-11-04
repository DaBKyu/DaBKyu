package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCouponDTO {
    private MemberEntity email;
	private CouponEntity couponSeqno;

    public MemberCouponDTO(MemberCouponEntity entity) {
        this.email = entity.getEmail();
        this.couponSeqno = entity.getCouponSeqno();
    }

    public MemberCouponEntity dtoToEntity(MemberCouponDTO dto) {
        MemberCouponEntity entity = MemberCouponEntity.builder()
                                                      .email(dto.getEmail())
                                                      .couponSeqno(dto.getCouponSeqno())
                                                      .build();
        return entity;
    }
}
