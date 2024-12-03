package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name="visitorLog")
@Table(name = "visitor_log", uniqueConstraints = @UniqueConstraint(columnNames = {"ip_address", "visit_date"}))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitorLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VISITORLOG_SEQ")
    @SequenceGenerator(name="VISITORLOG_SEQ", sequenceName = "visitorlog_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="visitor_log_seqno")
    private Long VisitorLogSeqno;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;

}
