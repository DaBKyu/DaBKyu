package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity(name="emailSendList")
@Table(name="email_send_list")
public class EmailSendListEntity {

    @Id
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
