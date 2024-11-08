package com.dabkyu.dabkyu.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.OrderProductDTO;
import com.dabkyu.dabkyu.entity.ShoppingCartEntity;

public interface ShoppingCartService {

    // 장바구니 보기
    public List<ShoppingCartEntity> getCartItems(String email);

    // 장바구니에 상품 넣기
    public void addToCart(
        OrderProductDTO orderProduct,
        MemberDTO memberDTO,
        Map<Long, Integer> relatedProducts,
        List<Long> productOptions
    );

    // 장바구니에 등록된 상품 수량 수정
    public void updateCartItemQuantity(String email, Long orderProductSeqno, int newQuantity);

    // 장바구니에서 특정 상품 삭제
    public void removeFromCart(String email, Long orderProductSeqno);

    // 장바구니에서 모든 상품 삭제
    public void clearCart(String email);

    // 결제
    public void pay(String email, List<Long> toPayOrderProducList, OrderInfoDTO orderInfo);

    // 장바구니에서 결제 완료된 상품 삭제
    public void removeProduct(String email);

    // 결제 취소
    public void cancelToPay(String email, Long orderSeqno);

    //교환 신청
    //public void requestExchange(String email, Long orderProductSeqno);

    //환불 신청
    public void refundRequest(String email, List<Long> orderProductSeqnos);

}
