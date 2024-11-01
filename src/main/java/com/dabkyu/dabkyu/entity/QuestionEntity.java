package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="question")
@Table(name="question")
public class QuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ")	
	@SequenceGenerator(name="QUESTION_SEQ", sequenceName = "question_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="que_seqno",nullable=false)
	private Long queSeqno;

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
