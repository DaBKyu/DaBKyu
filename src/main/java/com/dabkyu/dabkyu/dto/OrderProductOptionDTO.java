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
    private OrderProductEntity orderproductSeqno;
	private ProductOptionEntity optionSeqno;

    public OrderProductOptionDTO(OrderProductOptionEntity entity) {
        this.orderproductSeqno = entity.getOrderproductSeqno();
        this.optionSeqno = entity.getOptionSeqno();
    }

    public OrderProductOptionEntity dtoToEntity(OrderProductOptionDTO dto) {
        OrderProductOptionEntity entity = OrderProductOptionEntity.builder()
                                                                  .orderproductSeqno(dto.getOrderproductSeqno())
                                                                  .optionSeqno(dto.getOptionSeqno())
                                                                  .build();
        return entity;
    }
}
