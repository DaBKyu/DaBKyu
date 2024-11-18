package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

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
@Entity(name="report")
@Table(name="report")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPORT_SEQ")	
	@SequenceGenerator(name="REPORT_SEQ", sequenceName = "report_seq", 
		initialValue = 1, allocationSize = 1)
    @Column(name="report_seqno",nullable=false)
	private Long reportSeqno;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="email",nullable=false)
    private MemberEntity email;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="review_seqno",nullable=false)
    private ReviewEntity reviewSeqno;

    @Column(name="report_title",length=200,nullable=false)
	private String reportTitle;

    @Column(name="report_content",length=2000,nullable=false)
	private String reportContent;

    @Column(name="report_summary",length=50,nullable=false)
	private String reportSummary;

    @Column(name="report_date",nullable=false)
	private LocalDateTime reportDate;
    
}
