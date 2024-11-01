package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="notification")
@Table(name="notification")
public class NotificationEntity {

    @Id
    @Column(name="notification_seqno",nullable=false)
	private Long notificationSeqno;

    @Column(name="notification_name",nullable=false)
	private Long notificationName;

    @Column(name="notification_date",nullable=false)
	private LocalDateTime notificationDate;




}
