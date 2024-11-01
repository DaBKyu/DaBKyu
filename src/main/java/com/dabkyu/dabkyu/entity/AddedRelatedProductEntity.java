package com.dabkyu.dabkyu.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="addedRelatedProduct")
@Table(name="added_related_product")
@IdClass(AddedRelatedProductEntityID.class)
public class AddedRelatedProductEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="orderproduct_seqno", nullable = false)
	private OrderProductEntity orderProductSeqno;

    @Id
    @Column(name="relatedproduct_seqno")
	private Long relatedproductSeqno;



}
