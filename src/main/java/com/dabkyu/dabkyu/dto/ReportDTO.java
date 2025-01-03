package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ReportEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder 
public class ReportDTO {

    private Long reportSeqno;
	private MemberEntity email;
	private ReviewEntity reviewSeqno;
	private String reportTitle;
	private String reportContent;
	private String reportSummary;
	private LocalDateTime reportDate;
    private String processStatus;

    public ReportDTO(ReportEntity entity) {
        this.reportSeqno = entity.getReportSeqno();
        this.email = entity.getEmail();
        this.reviewSeqno = entity.getReviewSeqno();
        this.reportTitle = entity.getReportTitle();
        this.reportContent = entity.getReportContent();
        this.reportSummary = entity.getReportSummary();
        this.reportDate = entity.getReportDate();
        this.processStatus = entity.getProcessStatus();
    }

    public ReportEntity dtoToEntity(ReportDTO dto) {
        ReportEntity entity = ReportEntity.builder()
                                                              .reportSeqno(dto.getReportSeqno())
                                                              .email(dto.getEmail())
                                                              .reviewSeqno(dto.getReviewSeqno())
                                                              .reportTitle(dto.getReportTitle())
                                                              .reportContent(dto.getReportContent())
                                                              .reportSummary(dto.getReportSummary())
                                                              .reportDate(dto.getReportDate())
                                                              .processStatus(dto.getProcessStatus())
                                                              .build();
        return entity;
    }

}
