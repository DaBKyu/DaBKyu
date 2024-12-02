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
@Entity(name="orderProduct")
@Table(name="order_product")
@Builder
public class OrderProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERPRODUCT_SEQ")	
	@SequenceGenerator(name="ORDERPRODUCT_SEQ", sequenceName = "orderproduct_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="orderproduct_seqno",nullable=false)
	private Long orderProductSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="product_seqno", nullable = false)
	private ProductEntity productSeqno;

    @Column(name="review_yn",nullable=false)
	private String reviewYn;

    @Column(name="amount",nullable=false)
	private int amount;

}
