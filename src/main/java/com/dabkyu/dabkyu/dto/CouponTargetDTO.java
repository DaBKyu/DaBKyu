package com.dabkyu.dabkyu.dto;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.CouponTargetEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntity;

import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class CouponTargetDTO {

    private CouponEntity couponSeqno;
	private ProductEntity productSeqno;

    public CouponTargetDTO(CouponTargetEntity entity){
        this.couponSeqno = entity.getCouponSeqno();
        this.productSeqno = entity.getProductSeqno();
    }

    public CouponTargetEntity dtoToEntity(CouponTargetDTO dto){
        CouponTargetEntity entity = CouponTargetEntity.builder()
                                        .couponSeqno(dto.getCouponSeqno())
                                        .productSeqno(dto.getProductSeqno())
                                        .build();
        return entity;
    }
}
