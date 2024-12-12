package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
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
public class OrderInfoDTO {
    private Long orderSeqno;
	private MemberEntity email;
	private LocalDateTime orderDate;
	private String orderReq;
	private String orderStatus;
	private LocalDateTime exptDate;
	private String pay;
	private String resName;
	private String resAddress;
	private String resZipcode;
	private String resTelno;
	private int totalPrice;
    private int deliveryPrice;
    private List<OrderDetailDTO> orderDetails;

    public OrderInfoDTO(OrderInfoEntity entity) {
        this.orderSeqno = entity.getOrderSeqno();
        this.email = entity.getEmail();
        this.orderDate = entity.getOrderDate();
        this.orderReq = entity.getOrderReq();
        this.orderStatus = entity.getOrderStatus();
        this.exptDate = entity.getExptDate();
        this.pay = entity.getPay();
        this.resName = entity.getResName();
        this.resAddress = entity.getResAddress();
        this.resZipcode = entity.getResZipcode();
        this.resTelno = entity.getResTelno();
        this.totalPrice = entity.getTotalPrice();
        this.deliveryPrice = entity.getDeliveryPrice();
    }

    public OrderInfoEntity dtoToEntity(OrderInfoDTO dto) {
        OrderInfoEntity entity = OrderInfoEntity.builder()
                                                .orderSeqno(dto.getOrderSeqno())
                                                .email(dto.getEmail())
                                                .orderDate(dto.getOrderDate())
                                                .orderReq(dto.getOrderReq())
                                                .orderStatus(dto.getOrderStatus())
                                                .exptDate(dto.getExptDate())
                                                .pay(dto.getPay())
                                                .resName(dto.getResName())
                                                .resAddress(dto.getResAddress())
                                                .resZipcode(dto.getResZipcode())
                                                .resTelno(dto.getResTelno())
                                                .totalPrice(dto.getTotalPrice())
                                                .deliveryPrice(dto.getDeliveryPrice())
                                                .build();
         return entity;                                        
    }

    public static OrderInfoDTO matchOrderDetailDTO(OrderInfoEntity entity, List<OrderDetailDTO> details) {
        OrderInfoDTO dto = OrderInfoDTO.builder()
                                                                .orderSeqno(entity.getOrderSeqno())
                                                                .email(entity.getEmail())
                                                                .orderDate(entity.getOrderDate())
                                                                .orderReq(entity.getOrderReq())
                                                                .orderStatus(entity.getOrderStatus())
                                                                .exptDate(entity.getExptDate())
                                                                .pay(entity.getPay())
                                                                .resName(entity.getResName())
                                                                .resAddress(entity.getResAddress())
                                                                .resZipcode(entity.getResZipcode())
                                                                .resTelno(entity.getResTelno())
                                                                .totalPrice(entity.getTotalPrice())
                                                                .deliveryPrice(entity.getDeliveryPrice())
                                                                .orderDetails(details)
                                                                .build();
        return dto;
    }
}
