package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
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
public class ReviewDTO {
    private Long reviewSeqno;
	private ProductEntity productSeqno;
	private MemberEntity email;
	private String revWriter;
	private String revContent;
	private LocalDateTime revDate;
	private int likecnt;
	private int rate;
	private String secretYn;

	public ReviewDTO(ReviewEntity entity) {
		this.reviewSeqno = entity.getReviewSeqno();
		this.productSeqno = entity.getProductSeqno();
		this.email = entity.getEmail();
		this.revContent = entity.getRevContent();
		this.revDate = entity.getRevDate();
		this.likecnt = entity.getLikecnt();
		this.rate = entity.getRate();
		this.secretYn = entity.getSecretYn();
	}

	public ReviewEntity dtoToEntity(ReviewDTO dto) {
		ReviewEntity entity = ReviewEntity.builder()
										  .reviewSeqno(dto.getReviewSeqno())
										  .productSeqno(dto.getProductSeqno())
										  .email(dto.getEmail())
										  .revContent(dto.getRevContent())
										  .revDate(dto.getRevDate())
										  .likecnt(dto.getLikecnt())
										  .rate(dto.getRate())
										  .secretYn(dto.getSecretYn())
										  .build();
		return entity;
	}
}
