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

    public CouponDTO(CouponEntity entity) {
        this.couponSeqno = entity.getCouponSeqno();
        this.couponName = entity.getCouponName();
        this.couponType = entity.getCouponType();
        this.couponTarget = entity.getCouponTarget();
        this.couponInfo = entity.getCouponInfo();
        this.percentDiscount = entity.getPercentDiscount();
        this.amountDiscount = entity.getAmountDiscount();
        this.couponStartDate = entity.getCouponStartDate();
        this.couponEndDate = entity.getCouponEndDate();
        this.minOrder= entity.getMinOrder();
        this.couponRole= entity.getCouponRole();
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
