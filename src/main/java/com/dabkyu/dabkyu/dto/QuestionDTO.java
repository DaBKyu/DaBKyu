package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import com.dabkyu.dabkyu.entity.QuestionEntity;
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
public class QuestionDTO {
    private Long queSeqno;
	private String queType;
	private String queTitle;
	private String queWriter;
	private String queContent;
	private LocalDateTime queDate;
	private int queTypeNum;

    public QuestionDTO(QuestionEntity entity) {
        this.queSeqno = entity.getQueSeqno();
        this.queType = entity.getQueType();
        this.queTitle = entity.getQueTitle();
        this.queWriter = entity.getQueWriter();
        this.queContent = entity.getQueContent();
        this.queDate = entity.getQueDate();
        this.queTypeNum = entity.getQueTypeNum();
    }

    public QuestionEntity dtoToEntity(QuestionDTO dto) {
        QuestionEntity entity = QuestionEntity.builder()
                                              .queSeqno(dto.getQueSeqno())
                                              .queType(dto.getQueType())
                                              .queTitle(dto.getQueTitle())
                                              .queWriter(dto.getQueWriter())
                                              .queContent(dto.getQueContent())
                                              .queDate(dto.getQueDate())
                                              .queTypeNum(dto.getQueTypeNum())
                                              .build();
        return entity;
    }
}
