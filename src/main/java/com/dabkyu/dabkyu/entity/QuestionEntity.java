package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

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
@Entity(name="question")
@Table(name="question")
@Builder
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ")	
	@SequenceGenerator(name="QUESTION_SEQ", sequenceName = "question_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="que_seqno",nullable=false)
	private Long queSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="email",nullable=false)
    private MemberEntity email;

    @Column(name="que_type",length = 50, nullable=false)
	private String queType;

    @Column(name="que_title",length = 200, nullable=false)
	private String queTitle;

    @Column(name="que_writer",length = 50, nullable=false)
	private String queWriter;

    @Column(name="que_content",length = 2000, nullable=false)
	private String queContent;

    @Column(name="que_date", nullable=false)
	private LocalDateTime queDate;

    @Column(name="que_type_num", nullable=false)
	private int queTypeNum;
}
