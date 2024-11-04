package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
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
	private Long relatedproductSeqno;

    public AddedRelatedProductDTO(AddedRelatedProductEntity entity) {
        this.orderProductSeqno = entity.getOrderProductSeqno();
        this.relatedproductSeqno = entity.getRelatedproductSeqno();
    }

    public AddedRelatedProductEntity dtoToEntity(AddedRelatedProductDTO dto) {
        AddedRelatedProductEntity entity = AddedRelatedProductEntity.builder()
                                           .orderProductSeqno(dto.getOrderProductSeqno())
                                           .relatedproductSeqno(dto.getRelatedproductSeqno())
                                           .build();
        return entity;
    }
}
