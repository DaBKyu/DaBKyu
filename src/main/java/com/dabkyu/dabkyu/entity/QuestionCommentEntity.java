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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="questionComment")
@Table(name="question_comment")
@Builder
public class QuestionCommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_COMMENT_SEQ")	
	@SequenceGenerator(name="QUESTION_COMMENT_SEQ", sequenceName = "question_comment_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="question_comment_seqno",nullable=false)
	private Long questionCommentSeqno;

    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="que_seqno", nullable = false)
	private QuestionEntity queSeqno;

    @Column(name="com_content",length = 2000, nullable=false)
	private String comContent;

    @Column(name="com_date", nullable=false)
	private LocalDateTime comDate;

}
