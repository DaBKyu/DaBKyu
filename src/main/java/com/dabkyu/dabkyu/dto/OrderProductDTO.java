package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.OrderProductEntity;
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
public class OrderProductDTO {

    private Long orderProductSeqno;
	private Long productSeqno;
	private String reviewYn;
	private int amount;

    public OrderProductDTO(OrderProductEntity entity) {
        this.orderProductSeqno = entity.getOrderProductSeqno();
        this.productSeqno = entity.getProductSeqno().getProductSeqno();
        this.reviewYn = entity.getReviewYn();
        this.amount = entity.getAmount();
    }

    public OrderProductEntity dtoToEntity(OrderProductDTO dto, ProductEntity productEntity) {
        OrderProductEntity entity = OrderProductEntity.builder()
                                                      .orderProductSeqno(dto.getOrderProductSeqno())
                                                      .productSeqno(productEntity)
                                                      .reviewYn(dto.getReviewYn())
                                                      .amount(dto.getAmount())
                                                      .build();
        return entity;                                              
    }
}
