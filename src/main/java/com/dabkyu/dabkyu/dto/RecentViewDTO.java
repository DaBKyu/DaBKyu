package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.RecentViewEntity;
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
public class RecentViewDTO {
    private MemberEntity email;
	private ProductEntity productSeqno;

    public RecentViewDTO(RecentViewEntity entity) {
        this.email = entity.getEmail();
        this.productSeqno = entity.getProductSeqno();
    }

    public RecentViewEntity dtoToEntity(RecentViewDTO dto) {
        RecentViewEntity entity = RecentViewEntity.builder()
                                                  .email(dto.getEmail())
                                                  .productSeqno(dto.getProductSeqno())
                                                  .build();
        return entity;
    }

}
