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
@Entity(name="emailFile")
@Table(name="email_file")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailFileEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAILFILE_SEQ")
	@SequenceGenerator(name="EMAILFILE_SEQ", sequenceName = "email_file_seq", initialValue = 1, allocationSize = 1)
    @Column(name="emailfile_seqno", nullable=false)
    private Long emailfileSeqno;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email_seqno", nullable = false)
	private EmailEntity emailSeqno;
	
    @Column(name="org_filename", length=200, nullable=false)
	private String orgFilename;
	
	@Column(name="stored_filename", length=200, nullable=false)
	private String storedFilename;

}