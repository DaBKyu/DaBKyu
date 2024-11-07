package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
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
public class OrderProductOptionDTO {
    private OrderProductEntity orderProductSeqno;
	private ProductOptionEntity optionSeqno;

    public OrderProductOptionDTO(OrderProductOptionEntity entity) {
        this.orderProductSeqno = entity.getOrderProductSeqno();
        this.optionSeqno = entity.getOptionSeqno();
    }

    public OrderProductOptionEntity dtoToEntity(OrderProductOptionDTO dto) {
        OrderProductOptionEntity entity = OrderProductOptionEntity.builder()
                                                                  .orderProductSeqno(dto.getOrderProductSeqno())
                                                                  .optionSeqno(dto.getOptionSeqno())
                                                                  .build();
        return entity;
    }
}
