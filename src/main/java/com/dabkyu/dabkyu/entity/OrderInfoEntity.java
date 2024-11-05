package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="orderInfo")
@Table(name="order_info")
@Builder
public class OrderInfoEntity {

    @Id
    @Column(name="order_seqno",nullable=false)
	private Long orderSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email", nullable = false)
	private MemberEntity email;

    @Column(name="order_date", nullable=false)
	private LocalDateTime orderDate;

    @Column(name="order_req", length = 200, nullable=true)
	private String orderReq;

    @Column(name="order_status", length = 50, nullable=false)
	private String orderStatus;

    @Column(name="expt_date", nullable=false)
	private LocalDateTime exptDate;

    @Column(name="pay", length = 200, nullable=false)
	private String pay;

    @Column(name="res_name", length = 20, nullable=false)
	private String resName;

    @Column(name="res_address", length = 200, nullable=false)
	private String resAddress;

    @Column(name="res_zipcode", length = 20, nullable=false)
	private String resZipcode;

    @Column(name="res_telno", length = 20, nullable=false)
	private String resTelno;

    @Column(name="total_price", nullable=false)
	private int totalPrice;

}
