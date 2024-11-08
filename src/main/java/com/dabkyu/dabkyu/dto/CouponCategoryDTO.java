package com.dabkyu.dabkyu.dto;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.CouponCategoryEntity;
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
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
public class CouponCategoryDTO {
    
    private CouponEntity couponSeqno;
	private Category3Entity category3seqno;

    public CouponCategoryDTO(CouponCategoryEntity entity){
        this.couponSeqno = entity.getCouponSeqno();
        this.category3seqno = entity.getCategory3seqno();
    }

    public CouponCategoryEntity dtoToEntity(CouponCategoryDTO dto){
        CouponCategoryEntity entity = CouponCategoryEntity.builder()
                                        .couponSeqno(dto.getCouponSeqno())
                                        .category3seqno(dto.getCategory3seqno())
                                        .build();
        return entity;
    }
}
