package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name="email")
@Table(name="email")
@NoArgsConstructor
@AllArgsConstructor
public class EmailEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAIL_SEQ")	
	@SequenceGenerator(name="EMAIL_SEQ", sequenceName = "email_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="email_seqno")
	private Long emailSeqno;

    @Column(name="email_title", length=200, nullable=false)
	private String emailTitle;

    @Column(name="email_content", length=2000, nullable=false)
	private String emailContent;

    @Column(name="email_send_date", nullable=false)
	private LocalDateTime emailSendDate;
}
