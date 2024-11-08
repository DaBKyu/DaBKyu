package com.dabkyu.dabkyu.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name="couponCategory")
@Table(name="coupon_category")
@IdClass(CouponCategoryEntityID.class)
public class CouponCategoryEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="coupon_seqno", nullable = false)
	private CouponEntity couponSeqno;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="category3_seqno", nullable = false)
	private Category3Entity category3seqno;

}
