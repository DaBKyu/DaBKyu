package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVIEW_SEQ")	
	@SequenceGenerator(name="REVIEW_SEQ", sequenceName = "review_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="review_seqno",nullable=false)
	private Long reviewSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="product_seqno", nullable = false)
	private ProductEntity productSeqno;

    @Column(name="rev_writer",length=50,nullable=false)
	private String revWriter;

    @Column(name="rev_content",length=2000,nullable=false)
	private String revContent;

    @Column(name="rev_date",nullable=false)
	private LocalDateTime revDate;

    @Column(name="likecnt",nullable=false)
	private int likecnt;

    @Column(name="rate",nullable=false)
	private int rate;

    @Column(name="secret_yn",length = 2,nullable=false)
	private String secretYn;







    



    




}
