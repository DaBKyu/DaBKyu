package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberReviewLikeEntity;
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
public class MemberReviewLikeDTO {
    private MemberEntity email;
	private Long reviewSeqno;
    private LocalDateTime regdate;

    public MemberReviewLikeDTO(MemberReviewLikeEntity entity) {
        this.email = entity.getEmail();
        this.reviewSeqno = entity.getReviewSeqno();
        this.regdate = entity.getRegdate();
    }

    public MemberReviewLikeEntity dtoToEntity(MemberReviewLikeDTO dto) {
        MemberReviewLikeEntity entity = MemberReviewLikeEntity.builder()
                                                              .email(dto.getEmail())
                                                              .reviewSeqno(dto.getReviewSeqno())
                                                              .regdate(dto.getRegdate())
                                                              .build();
        return entity;
    }
}
