package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntity;
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
public class ShoppingCartDTO {
    private MemberEntity email;
	private OrderProductEntity orderProductSeqno;

    public ShoppingCartDTO(ShoppingCartEntity entity) {
        this.email = entity.getEmail();
        this.orderProductSeqno = entity.getOrderProductSeqno();
    }

    public ShoppingCartEntity dtoToEntity(ShoppingCartDTO dto) {
        ShoppingCartEntity entity = ShoppingCartEntity.builder()
                                                      .email(dto.getEmail())
                                                      .orderProductSeqno(dto.getOrderProductSeqno())
                                                      .build();
        return entity;
    }
}
