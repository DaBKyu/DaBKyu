package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

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
public class ReviewFileDTO {
    private Long reviewFileSeqno;
	private ReviewEntity reviewSeqno;
	private String orgFilename;
	private String storedFilename;

	public ReviewFileDTO(ReviewFileEntity entity) {
		this.reviewFileSeqno = entity.getReviewFileSeqno();
		this.reviewSeqno = entity.getReviewSeqno();
		this.orgFilename = entity.getOrgFilename();
		this.storedFilename = entity.getStoredFilename();
	}

	public ReviewFileEntity dtoToEntity(ReviewFileDTO dto) {
		ReviewFileEntity entity = ReviewFileEntity.builder()
												  .reviewFileSeqno(dto.getReviewFileSeqno())
												  .reviewSeqno(dto.getReviewSeqno())
												  .orgFilename(dto.getOrgFilename())
												  .storedFilename(dto.getStoredFilename())
												  .build();
		return entity;
	}

}
