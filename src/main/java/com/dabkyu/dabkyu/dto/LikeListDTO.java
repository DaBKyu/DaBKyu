package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;

import com.dabkyu.dabkyu.entity.LikeListEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
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
public class LikeListDTO {
	private MemberEntity email;
	private ProductEntity productSeqno;
    private LocalDateTime likeDate;

    public LikeListDTO(LikeListEntity entity) {
        this.email = entity.getEmail();
        this.productSeqno = entity.getProductSeqno();
        this.likeDate = entity.getLikeDate();
    }

    public LikeListEntity dtoToEntity(LikeListDTO dto) {
        LikeListEntity likeListEntity = LikeListEntity.builder()
                                                      .email(dto.getEmail())
                                                      .productSeqno(dto.getProductSeqno())
                                                      .likeDate(dto.getLikeDate())
                                                      .build();
        return likeListEntity;
    }
}
