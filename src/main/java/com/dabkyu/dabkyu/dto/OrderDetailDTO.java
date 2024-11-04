package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
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
public class OrderDetailDTO {
    private Long orderDetailSeqno;
	private OrderInfoEntity orderSeqno;
	private OrderProductEntity orderproduct_seqno;

    public OrderDetailDTO(OrderDetailEntity entity) {
        this.orderDetailSeqno = entity.getOrderDetailSeqno();
        this.orderSeqno = entity.getOrderSeqno();
        this.orderproduct_seqno = entity.getOrderproduct_seqno();
    }

    public OrderDetailEntity dtoToEntity(OrderDetailDTO dto) {
        OrderDetailEntity entity = OrderDetailEntity.builder()
                                                    .orderDetailSeqno(dto.getOrderDetailSeqno())
                                                    .orderSeqno(dto.getOrderSeqno())
                                                    .orderproduct_seqno(dto.getOrderproduct_seqno())
                                                    .build();
        return entity;
    }
}
