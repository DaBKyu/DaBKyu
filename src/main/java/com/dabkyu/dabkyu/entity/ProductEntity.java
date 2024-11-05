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
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="product")
@Table(name="product")
@Builder
public class ProductEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ")	
	@SequenceGenerator(name="PRODUCT_SEQ", sequenceName = "product_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="product_seqno",nullable=false)
	private Long productSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="category3_seqno", nullable = false)
	private Category3Entity category3Seqno;

    @Column(name="product_info",length = 200,nullable=false)
	private String productInfo;

    @Column(name="price",nullable=false)
	private int price;

    @Column(name="stock_amount",nullable=false)
	private int stockAmount;

    @Column(name="deliveryis_free",length = 2,nullable=false)
	private String deliveryisFree;

    @Column(name="likecnt",nullable=false)
	private int likecnt;

    @Column(name="info_org_image",length = 200,nullable=true)
	private String infoOrgImage;

    @Column(name="info_stored_image",length = 200,nullable=true)
	private String infoStoredImage;

}
