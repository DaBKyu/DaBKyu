package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.OrderProductDTO;
import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.entity.RelatedProductEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntity;
import com.dabkyu.dabkyu.entity.repository.AddedRelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.entity.repository.OrderDetailRepository;
import com.dabkyu.dabkyu.entity.repository.OrderInfoRepository;
import com.dabkyu.dabkyu.entity.repository.OrderProductOptionRepository;
import com.dabkyu.dabkyu.entity.repository.OrderProductRepository;
import com.dabkyu.dabkyu.entity.repository.ProductOptionRepository;
import com.dabkyu.dabkyu.entity.repository.ProductRepository;
import com.dabkyu.dabkyu.entity.repository.RelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.ShoppingCartRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final OrderProductRepository orderProductRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final AddedRelatedProductRepository addedRelatedProductRepository;
    private final OrderProductOptionRepository orderProductOptionRepository; 
    private final ProductOptionRepository productOptionRepository;
    private final OrderInfoRepository orderInfoRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final MemberRepository memberRepository;
    private final RelatedProductRepository relatedProductRepository;
    private final ProductRepository productRepository;
    
    // 장바구니 보기
    @Override
    public List<ShoppingCartEntity> getCartItems(String email) {
        return shoppingCartRepository.findByEmail_Email(email);
    }
    
    // 장바구니에 상품 넣기
    @Override
    public void addToCart(
        OrderProductDTO orderProduct,
        MemberDTO memberDTO,
        Map<Long, Integer> relatedProducts,
        List<Long> productOptions
    ) {
        // OrderProductEntity 생성 및 저장
        orderProductRepository.save(orderProduct.dtoToEntity(orderProduct));

        // 주문 추가 상품과 주문 추가 옵션이 있는지 확인후 저장
        if(!relatedProducts.isEmpty()){
            for (Map.Entry<Long,Integer> entry : relatedProducts.entrySet()) {
                RelatedProductEntity relatedProductEntity = relatedProductRepository.findById(entry.getKey()).get();
                OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProduct.getOrderProductSeqno()).get();
                AddedRelatedProductEntity addedRelatedProductEntity = AddedRelatedProductEntity.builder()
                                                                                               .orderProductSeqno(orderProductEntity)
                                                                                               .relatedProductSeqno(relatedProductEntity)
                                                                                               .amount(entry.getValue())
                                                                                               .build();
            addedRelatedProductRepository.save(addedRelatedProductEntity);                                                                              
            }
       
        }

        if(!productOptions.isEmpty()){
            for (Long seqno:productOptions) {
                ProductOptionEntity productOptionEntity = productOptionRepository.findById(seqno).get();
                OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProduct.getOrderProductSeqno()).get();
                OrderProductOptionEntity orderProductOptionEntity = OrderProductOptionEntity.builder()
                                                                                               .orderProductSeqno(orderProductEntity)
                                                                                               .optionSeqno(productOptionEntity)
                                                                                               .build();
            orderProductOptionRepository.save(orderProductOptionEntity);                                                                              
            }
       
        }

        MemberEntity memberEntity = memberRepository.findById(memberDTO.getEmail()).get();
        ShoppingCartEntity shoppingCartEntity = ShoppingCartEntity.builder()
                                                                  .email(memberEntity)
                                                                  .orderProductSeqno(orderProduct.dtoToEntity(orderProduct))
                                                                  .build();
        shoppingCartRepository.save(shoppingCartEntity);
    }


    // 장바구니에 등록된 상품 수량 수정
    @Override
    public void updateCartItemQuantity(String email, Long orderProductSeqno, int newQuantity) {
        // 장바구니 항목 가져오기
        ShoppingCartEntity cartItem = shoppingCartRepository.findByEmail_EmailAndOrderProductSeqno_OrderProductSeqno(email, orderProductSeqno);
        
        // 장바구니에 해당 상품이 존재하는지 확인
        if (cartItem == null) {
            throw new RuntimeException("장바구니에 해당 상품이 없습니다.");
        }

        // 수량이 0이면 장바구니에서 해당 항목을 삭제
        if (newQuantity <= 0) {
            shoppingCartRepository.delete(cartItem);
        } else {
            // 장바구니 항목의 수량 업데이트
            OrderProductEntity orderProduct = cartItem.getOrderProductSeqno();
            orderProduct.setAmount(newQuantity);
            orderProductRepository.save(orderProduct);
        }
    }

    // 장바구니에서 특정 상품 삭제
    @Override
    public void removeFromCart(String email, Long orderProductSeqno) {
        ShoppingCartEntity itemToRemove = shoppingCartRepository.findByEmail_EmailAndOrderProductSeqno_OrderProductSeqno(email, orderProductSeqno);
        
        // 해당 상품이 장바구니에 존재하는 경우 삭제
        if (itemToRemove != null) {
            shoppingCartRepository.delete(itemToRemove);
        } else {
            throw new RuntimeException("장바구니에 해당 상품이 없습니다.");
        }
    }

    // 장바구니에서 모든 상품 삭제
    @Override
    public void clearCart(String email) {
        List<ShoppingCartEntity> cartItems = shoppingCartRepository.findByEmail_Email(email);
        
        // 모든 상품 삭제
        shoppingCartRepository.deleteAll(cartItems);
    }


    // 결제
    @Transactional
    @Override
    public void pay(String email, List<Long> toPayOrderProductList, OrderInfoDTO orderInfo) {

        //결제할때 재고 확인 방법 고민..
        // 재고 수량 확인
        for (Long orderProductSeqno : toPayOrderProductList) {
            OrderProductEntity orderProduct = orderProductRepository.findById(orderProductSeqno).get();
            
            ProductEntity product = orderProduct.getProductSeqno();
            int availableStock = product.getStockAmount();
            
            // 주문한 수량이 재고보다 많은지 확인
            if (orderProduct.getAmount() > availableStock) {
                throw new RuntimeException("재고가 부족합니다: " + product.getProductName() + "의 재고 수량이 충분하지 않습니다.");
            }
        }

    
        // 회원 정보 가져오기
        MemberEntity memberEntity = memberRepository.findById(email).get();

        // 총 가격
        Map<String,Object> calculateResult = calculatePrice(toPayOrderProductList);

        // 주문 정보 생성
        OrderInfoEntity orderInfoEntity = OrderInfoEntity.builder()
                                                          .email(memberEntity)
                                                          .orderDate(LocalDateTime.now())
                                                          .orderReq(orderInfo.getOrderReq())
                                                          .orderStatus("결제 완료")
                                                          .exptDate(LocalDateTime.now().plusDays(1))
                                                          .pay(orderInfo.getPay())
                                                          .resName(orderInfo.getResName())
                                                          .resAddress(orderInfo.getResAddress())
                                                          .resTelno(orderInfo.getResTelno())
                                                          .totalPrice((Integer) calculateResult.get("totalPrice"))
                                                          .deliveryPrice(calculateResult.get("isFree")=="Y"?0:3000)
                                                          .build();
        orderInfoRepository.save(orderInfoEntity);
                
        for (Long orderProductSeqno : toPayOrderProductList) {
            // 각 orderPorduct 
            OrderProductEntity orderProduct = orderProductRepository.findById(orderProductSeqno).get();
            

            // 주문 상세 정보 저장
            OrderDetailEntity orderDetail = OrderDetailEntity.builder()
                                                             .orderSeqno(orderInfoEntity)
                                                             .orderProductSeqno(orderProduct)
                                                             .build();
            orderDetailRepository.save(orderDetail);
        }

        // 주문 수량에 따라 제품 재고 수량 조정
        for (Long orderProductSeqno : toPayOrderProductList) {
            OrderProductEntity orderProduct = orderProductRepository.findById(orderProductSeqno).get();
            ProductEntity product = orderProduct.getProductSeqno();
            int newStock = product.getStockAmount() - orderProduct.getAmount();
            if (newStock < 0) { 
                throw new RuntimeException("재고가 부족합니다.");
            }
            product.setStockAmount(newStock);
            productRepository.save(product);
        }
}

   // 결제 완료된 제품만 장바구니에서 삭제
   @Transactional
   @Override
   public void removeProduct(String email) {
   List<OrderInfoEntity> orderInfoList = orderInfoRepository.findByEmail_Email(email);

    for (OrderInfoEntity orderInfo : orderInfoList) {
        // 주문 상태가 "결제 완료"
        if ("결제 완료".equals(orderInfo.getOrderStatus())) {
            
            // 해당 주문에 포함된 상품들을 찾습니다
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderSeqno(orderInfo);
            
            for (OrderDetailEntity orderDetail : orderDetails) {
                OrderProductEntity orderProduct = orderDetail.getOrderProductSeqno();


                orderProductRepository.delete(orderProduct);
                }
            }
        }
    }      

    // 총 가격 계산 메서드
    private Map<String,Object> calculatePrice(List<Long> toPayOrderProductList) {
        int totalPrice = 0;
        String isFree = "Y";
        for (Long orderProductSeqno : toPayOrderProductList) {
            
            OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProductSeqno).get();
            //배송비 무료 확인
            ProductEntity productEntity = orderProductEntity.getProductSeqno();
            if (productEntity.getDeliveryisFree()=="N") isFree = "N";
            
            // 제품 가격 + 옵션 가격
            int productPrice = productEntity.getPrice();
            List<OrderProductOptionEntity> orderProductOptionEntities = orderProductOptionRepository.findByOrderProductSeqno(orderProductSeqno);
            if (!orderProductOptionEntities.isEmpty()) {
                for (OrderProductOptionEntity orderProductOptionEntity : orderProductOptionEntities) {
                    ProductOptionEntity productOptionEntity = orderProductOptionEntity.getOptionSeqno();
                    productPrice += productOptionEntity.getOptPrice();
                }
            }

            totalPrice += productPrice * orderProductEntity.getAmount(); // 각 제품의 가격과 장바구니 수량에 따라 총 가격 계산

            // 추가 상품 가격 계산
            List<AddedRelatedProductEntity> addedRelatedProducts = addedRelatedProductRepository.findByOrderProductSeqno(orderProductSeqno);
            if (!addedRelatedProducts.isEmpty()) {
                for (AddedRelatedProductEntity addedProduct : addedRelatedProducts) {
                    RelatedProductEntity relatedProductEntity = addedProduct.getRelatedProductSeqno();
                    int relatedProductPrice = relatedProductEntity.getRelatedproductPrice();
                    totalPrice += relatedProductPrice * addedProduct.getAmount(); // 추가 상품의 가격을 포함
                }
            }
        }
        Map<String,Object> priceInfo = new HashMap<>();
        priceInfo.put("totalPrice", totalPrice);
        priceInfo.put("isFree", isFree);
        return priceInfo;
    }

    //결제 취소
    public void cancelToPay(String email, Long orderSeqno) {
        
        // 주문 정보 조회
        OrderInfoEntity orderInfo = orderInfoRepository.findById(orderSeqno).get();

        // 주문자가 맞는지 확인
        if (!orderInfo.getEmail().getEmail().equals(email)) {
            throw new RuntimeException("이 주문은 해당 회원이 요청할 수 없습니다.");
        }

        // 주문 상태 변경 (결제 취소)
        orderInfo.setOrderStatus("결제 취소");
        orderInfo.setExptDate(null);  // 예상 배송 날짜 초기화
        orderInfo.setDeliveryPrice(0); // 배송비 초기화 (배송 취소된 경우)
        orderInfoRepository.save(orderInfo);

        // 주문 상품 목록 조회
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderSeqno(orderInfo);

        // 주문 상세 정보 처리 (상품 개별 취소)
        for (OrderDetailEntity orderDetail : orderDetails) {
            // 주문 상품 정보
            OrderProductEntity orderProduct = orderDetail.getOrderProductSeqno();

            orderDetail.setCancelYn("Y");
            orderDetailRepository.save(orderDetail);

            // 주문된 상품의 재고 복구
            ProductEntity product = orderProduct.getProductSeqno();
            product.setStockAmount(product.getStockAmount() + orderProduct.getAmount());
            productRepository.save(product);
        }
    }

    // 환불 신청 처리 
    public void refundRequest(String email, Long orderSeqno) {
        // 주문 상품 조회
       OrderInfoEntity orderInfo = orderInfoRepository.findByEmail_EmailAndOrderSeqno(email, orderSeqno);

        // 주문 상세 정보 업데이트 (환불 상태로 변경)
        //OrderDetailEntity orderDetail = orderDetailRepository.findByOrderSeqno_OrderSeqno(orderInfo.getOrderSeqno());
        //orderDetail.setRefundYn("Y");

        //주문 정보 업데이트 (환불 상태로 변경)
        //orderInfo = orderInfoRepository.findByOrderSeqno(orderDetail.getOrderSeqno());
        orderInfo.setOrderStatus("환불 신청");
        orderInfoRepository.save(orderInfo);
    }

}
   
