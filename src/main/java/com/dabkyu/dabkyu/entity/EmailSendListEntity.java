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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="emailSendList")
@Table(name="email_send_list")
public class EmailSendListEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMAILSENDLIST_SEQ")	
	@SequenceGenerator(name="EMAILSENDLIST_SEQ", sequenceName = "emailsendlist_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="email_sendlist_seqno")
	private Long emailSendlistSeqno;
	
	@Column(name="target_category", length=50, nullable=false)
	private String targetCategory;

    @Column(name="email_title", length=200, nullable=false)
	private String emailTitle;

    @Column(name="email_content", length=2000, nullable=false)
	private String emailContent;

    @Column(name="email_send_date", nullable=false)
	private LocalDateTime emailSendDate;
}
