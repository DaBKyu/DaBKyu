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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="notification")
@Table(name="notification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ")	
	@SequenceGenerator(name="NOTIFICATION_SEQ", sequenceName = "notification_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="notification_seqno",nullable=false)
	private Long notificationSeqno;

    @Column(name="notification_name",nullable=false)
	private String notificationName;

    @Column(name="notification_date",nullable=false)
	private LocalDateTime notificationDate;




}
