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
@Entity(name="questionFile")
@Table(name="question_file")
@Builder
public class QuestionFileEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTIONFILE_SEQ")
	@SequenceGenerator(name="QUESTIONFILE_SEQ", sequenceName = "question_file_seq", initialValue = 1, allocationSize = 1)
	@Column(name="questionfile_seqno", nullable=false)
    private Long questionFileSeqno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="que_seqno", nullable = false)
	private QuestionEntity queSeqno;
	
    @Column(name="org_filename", length=200, nullable=false)
	private String orgFilename;
	
	@Column(name="stored_filename", length=200, nullable=false)
	private String storedFilename;

}
