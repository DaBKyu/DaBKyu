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
@Entity(name="relatedProduct")
@Table(name="related_product")
@Builder
public class RelatedProductEntity {
    
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RELATEDPRODUCT_SEQ")	
	@SequenceGenerator(name="RELATEDPRODUCT_SEQ", sequenceName = "relatedproduct_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="relatedproduct_seqno",nullable=false)
	private Long relatedProductSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="product_seqno", nullable = false)
	private ProductEntity productSeqno;

    @Column(name="relatedproduct_category",length=20,nullable=false)
	private String relatedproductCategory;

    @Column(name="relatedproduct_name",length=20,nullable=false)
	private String relatedproductName;

    @Column(name="relatedproduct_price",nullable=false)
	private int relatedproductPrice;


}
