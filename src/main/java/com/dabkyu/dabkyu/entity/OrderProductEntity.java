package com.dabkyu.dabkyu.entity;

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
@Entity(name="orderProduct")
@Table(name="order_product")
@Builder
public class OrderProductEntity {

    @Id
    @Column(name="orderproduct_seqno",nullable=false)
	private Long orderproductSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="product_seqno", nullable = false)
	private ProductEntity productSeqno;

    @Column(name="review_yn",nullable=false)
	private String reviewYn;

    @Column(name="amount",nullable=false)
	private int amount;

    @Column(name="price",nullable=false)
	private int price;







}
