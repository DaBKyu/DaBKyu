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
@Entity(name="reviewFile")
@Table(name="review_file")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewFileEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REVIEWFILE_SEQ")
	@SequenceGenerator(name="REVIEWFILE_SEQ", sequenceName = "review_file_seq", initialValue = 1, allocationSize = 1)
	@Column(name="reviewfile_seqno", nullable=false)
    private Long reviewFileSeqno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="review_seqno", nullable = false)
	private ReviewEntity reviewSeqno;
	
    @Column(name="org_filename", length=200, nullable=false)
	private String orgFilename;
	
	@Column(name="stored_filename", length=200, nullable=false)
	private String storedFilename;

}
