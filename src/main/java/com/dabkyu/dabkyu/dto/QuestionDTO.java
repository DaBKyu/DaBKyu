package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
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
    private MemberEntity email;
    private ProductEntity productSeqno;
	private String queType;
	private String queTitle;
	private String queWriter;
	private String queContent;
	private LocalDateTime queDate;
	

    public QuestionDTO(QuestionEntity entity) {
        this.queSeqno = entity.getQueSeqno();
        this.email = entity.getEmail();
        this.productSeqno = entity.getProductSeqno();
        this.queType = entity.getQueType();
        this.queTitle = entity.getQueTitle();
        this.queContent = entity.getQueContent();
        this.queDate = entity.getQueDate();
    }

    public QuestionEntity dtoToEntity(QuestionDTO dto) {
        QuestionEntity entity = QuestionEntity.builder()
                                              .queSeqno(dto.getQueSeqno())
                                              .email(dto.getEmail())
                                              .productSeqno(dto.getProductSeqno())
                                              .queType(dto.getQueType())
                                              .queTitle(dto.getQueTitle())
                                              .queContent(dto.getQueContent())
                                              .queDate(dto.getQueDate())
                                              .build();
        return entity;
    }
}
