package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="questionComment")
@Table(name="question_comment")
public class QuestionCommentEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="que_seqno", nullable = false)
	private QuestionEntity queSeqno;

    @Column(name="com_writer",length = 50, nullable=false)
	private String comWriter;

    @Column(name="com_content",length = 2000, nullable=false)
	private String comContent;

    @Column(name="com_date", nullable=false)
	private LocalDateTime comDate;

}
