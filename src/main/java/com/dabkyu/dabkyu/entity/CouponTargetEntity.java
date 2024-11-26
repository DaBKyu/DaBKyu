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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="couponTarget")
@Table(name="coupon_target")
@IdClass(CouponTargetEntityID.class)
@NoArgsConstructor
@AllArgsConstructor
public class CouponTargetEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="coupon_seqno", nullable = false)
	private CouponEntity couponSeqno;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="product_seqno", nullable = false)
	private ProductEntity productSeqno;

}
