package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.RelatedProductEntity;

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
public class AddedRelatedProductDTO {
	private OrderProductEntity orderProductSeqno;
	private RelatedProductEntity relatedProductSeqno;

    public AddedRelatedProductDTO(AddedRelatedProductEntity entity) {
        this.orderProductSeqno = entity.getOrderProductSeqno();
        this.relatedProductSeqno = entity.getRelatedProductSeqno();
    }

    public AddedRelatedProductEntity dtoToEntity(AddedRelatedProductDTO dto) {
        AddedRelatedProductEntity entity = AddedRelatedProductEntity.builder()
                                           .orderProductSeqno(dto.getOrderProductSeqno())
                                           .relatedProductSeqno(dto.getRelatedProductSeqno())
                                           .build();
        return entity;
    }
}
