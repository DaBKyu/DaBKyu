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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="productOption")
@Table(name="product_option")
@Builder
public class ProductOptionEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTOPTION_SEQ")	
	@SequenceGenerator(name="PRODUCTOPTION_SEQ", sequenceName = "productoption_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="option_seqno",nullable=false)
	private Long optionSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="product_seqno", nullable = false)
	private ProductEntity productSeqno;

    @Column(name="opt_category", length=20, nullable=false)
	private String optCategory;

    @Column(name="opt_name",length=20, nullable=false)
	private String optName;

    @Column(name="opt_price", nullable=false)
	private int optPrice;
    










}
