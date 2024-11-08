package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.QuestionCommentEntity;
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
public class QuestionCommentDTO {
    private QuestionEntity queSeqno;
    private MemberEntity email;
	private String comWriter;
	private String comContent;
	private LocalDateTime comDate;

    public QuestionCommentDTO(QuestionCommentEntity entity) {
        this.queSeqno = entity.getQueSeqno();
        this.email = entity.getEmail();
        this.comContent = entity.getComContent();
        this.comDate = entity.getComDate();
    }

    public QuestionCommentEntity dtoToEntity(QuestionCommentDTO dto) {
        QuestionCommentEntity entity = QuestionCommentEntity.builder()
                                                            .queSeqno(dto.getQueSeqno())
                                                            .email(dto.getEmail())
                                                            .comContent(dto.getComContent())
                                                            .comDate(dto.getComDate())
                                                            .build();
        return entity;
    }
}
