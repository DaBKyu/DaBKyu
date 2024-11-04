package com.dabkyu.dabkyu.dto;

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

    public LikeListDTO(LikeListEntity entity) {
        this.email = entity.getEmail();
        this.productSeqno = entity.getProductSeqno();
    }

    public LikeListEntity dtoToEntity(LikeListDTO dto) {
        LikeListEntity likeListEntity = LikeListEntity.builder()
                                                      .email(dto.getEmail())
                                                      .productSeqno(dto.getProductSeqno())
                                                      .build();
        return likeListEntity;
    }
}
