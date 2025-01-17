package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
@Entity(name="coupon")
@Table(name="coupon")  
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COUPON_SEQ")	
	@SequenceGenerator(name="COUPON_SEQ", sequenceName = "coupon_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="coupon_seqno", nullable = false)
    private Long couponSeqno;

    @Column(name="coupon_name", length=20, nullable = false)
    private String couponName;

    @Column(name="coupon_type", length=20, nullable = false)
    private String couponType;

    @Column(name="coupon_info", length=20, nullable = false)
    private String couponInfo;

    @Column(name="percent_discount", nullable = true)
    private Integer percentDiscount;

    @Column(name="amount_discount", nullable = true)
    private Integer amountDiscount;

    @Column(name="coupon_start_date", nullable = false)
    private LocalDateTime couponStartDate;

    @Column(name="coupon_end_date", nullable = false)
    private LocalDateTime couponEndDate;

    @Column(name="min_order", nullable = false)
    private int minOrder;

    @Column(name="coupon_role", length=20, nullable = false)
    private String couponRole;

    @Column(name="isDupl", length=2, nullable = false)
    private String isDupl;

    @Column(name="coupon_code", length=20, nullable = false)
    private String couponCode;

}
