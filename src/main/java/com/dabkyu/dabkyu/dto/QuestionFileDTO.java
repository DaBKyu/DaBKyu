package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;

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
public class QuestionFileDTO {
    private Long questionfileSeqno;
	private QuestionEntity queSeqno;
	private String orgFilename;
	private String storedFilename;

	public QuestionFileDTO(QuestionFileEntity entity) {
		this.questionfileSeqno = entity.getQuestionfileSeqno(); 
		this.queSeqno = entity.getQueSeqno();
		this.orgFilename = entity.getOrgFilename();
		this.storedFilename = entity.getStoredFilename();
	}

	public QuestionFileEntity dtoToEntity(QuestionFileDTO dto) {
		QuestionFileEntity entity = QuestionFileEntity.builder()
													  .questionfileSeqno(dto.getQuestionfileSeqno())
													  .queSeqno(dto.getQueSeqno())
													  .orgFilename(dto.getOrgFilename())
													  .storedFilename(dto.getStoredFilename())
													  .build();
		return entity;
	}
}
