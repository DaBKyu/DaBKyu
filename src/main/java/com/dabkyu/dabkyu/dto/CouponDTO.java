package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import com.dabkyu.dabkyu.entity.CouponEntity;

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
public class CouponDTO {

    private Long couponSeqno;
    private String couponName;
    private String couponType;
    private String couponTarget;
    private String couponInfo;
    private int percentDiscount;
    private int amountDiscount;
    private LocalDateTime couponStartDate;
    private LocalDateTime couponEndDate;
    private int minOrder;
    private String couponRole;

    public CouponDTO(CouponEntity couponEntity) {
        this.couponSeqno = couponEntity.getCouponSeqno();
        this.couponName = couponEntity.getCouponName();
        this.couponType = couponEntity.getCouponType();
        this.couponTarget = couponEntity.getCouponTarget();
        this.couponInfo = couponEntity.getCouponInfo();
        this.percentDiscount = couponEntity.getPercentDiscount();
        this.amountDiscount = couponEntity.getAmountDiscount();
        this.couponStartDate = couponEntity.getCouponStartDate();
        this.couponEndDate = couponEntity.getCouponEndDate();
        this.minOrder= couponEntity.getMinOrder();
        this.couponRole= couponEntity.getCouponRole();
    }

    public CouponEntity dtoToEntity(CouponDTO dto) {
        
        CouponEntity entity = CouponEntity.builder()
                                          .couponSeqno(dto.getCouponSeqno())
                                          .couponName(dto.getCouponName())
                                          .couponType(dto.getCouponType())
                                          .couponTarget(dto.getCouponTarget())
                                          .couponInfo(dto.getCouponInfo())
                                          .percentDiscount(dto.getPercentDiscount())
                                          .amountDiscount(dto.getAmountDiscount())
                                          .couponStartDate(dto.getCouponStartDate())                                           
                                          .couponEndDate(dto.getCouponEndDate())
                                          .build();
        return entity;
    }


}
