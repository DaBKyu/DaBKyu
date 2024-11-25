package com.dabkyu.dabkyu.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name="orderDetail")
@Table(name="order_detail")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERDETAIL_SEQ")	
	@SequenceGenerator(name="ORDERDETAIL_SEQ", sequenceName = "orderdetail_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="order_detail_seqno",nullable=false)
	private Long orderDetailSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="order_seqno", nullable = false)
	private OrderInfoEntity orderSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="orderproduct_seqno", nullable = false)
	private OrderProductEntity orderProductSeqno;

	@Column(name="cancel_yn", length=2, nullable=false)
	private String cancelYn;

	@Column(name="refund_yn", length=2, nullable=false)
	private String refundYn;


}
