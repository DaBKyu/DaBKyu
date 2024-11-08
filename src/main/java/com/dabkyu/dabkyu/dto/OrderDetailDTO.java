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
	private OrderProductEntity orderProductSeqno;
    private String cancelYn;
    private String refundYn;

    public OrderDetailDTO(OrderDetailEntity entity) {
        this.orderDetailSeqno = entity.getOrderDetailSeqno();
        this.orderSeqno = entity.getOrderSeqno();
        this.orderProductSeqno = entity.getOrderProductSeqno();
        this.cancelYn = entity.getCancelYn();
        this.refundYn = entity.getRefundYn();
    }

    public OrderDetailEntity dtoToEntity(OrderDetailDTO dto) {
        OrderDetailEntity entity = OrderDetailEntity.builder()
                                                    .orderDetailSeqno(dto.getOrderDetailSeqno())
                                                    .orderSeqno(dto.getOrderSeqno())
                                                    .orderProductSeqno(dto.getOrderProductSeqno())
                                                    .cancelYn(dto.getCancelYn())
                                                    .refundYn(dto.getRefundYn())
                                                    .build();
        return entity;
    }
}
