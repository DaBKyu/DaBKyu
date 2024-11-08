package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.dto.OrderInfoDTO;
import com.dabkyu.dabkyu.dto.OrderProductDTO;
import com.dabkyu.dabkyu.dto.QuestionDTO;
import com.dabkyu.dabkyu.entity.AddedRelatedProductEntity;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
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
        return shoppingCartRepository.findByEmail(email);
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
        ShoppingCartEntity cartItem = shoppingCartRepository.findByEmailAndOrderProductSeqno(email, orderProductSeqno);
        
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
        ShoppingCartEntity itemToRemove = shoppingCartRepository.findByEmailAndOrderProductSeqno(email, orderProductSeqno);
        
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
        List<ShoppingCartEntity> cartItems = shoppingCartRepository.findByEmail(email);
        
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
   List<OrderInfoEntity> orderInfoList = orderInfoRepository.findByEmail(email);

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

        // 주문 상태가 이미 취소되었는지 확인
        if ("취소 완료".equals(orderInfo.getOrderStatus())) {
            throw new RuntimeException("이미 취소된 주문입니다.");
        }
    
        // 주문 상품 목록 조회
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderSeqno(orderInfo);
    
        // 재고 복원 처리
        for (OrderDetailEntity orderDetail : orderDetails) {
            OrderProductEntity orderProduct = orderDetail.getOrderProductSeqno();
            ProductEntity product = orderProduct.getProductSeqno();
    
            int restoredStock = product.getStockAmount() + orderProduct.getAmount();
            product.setStockAmount(restoredStock);
            productRepository.save(product);
        }
    
        // 주문 상태 업데이트
        orderInfo.setOrderStatus("취소 완료");
        orderInfoRepository.save(orderInfo);
    }

/*
    // 환불 신청 처리 
    public void refundRequest(String email, List<Long> orderProductSeqnos) {
        // 주문 상품 조회
        List<OrderProductEntity> orderProducts = orderProductRepository.findByEmailAndSeqnos(email, orderProductSeqnos);

        // 환불 처리
        orderProducts.forEach(orderProduct -> {
            orderProduct.setReviewYn("N");  // 환불 시 리뷰 상태 초기화
            orderProductRepository.save(orderProduct);

            // 주문 상세 정보 업데이트 (환불 상태로 변경)
            OrderDetailEntity orderDetail = orderDetailRepository.findByOrderProductSeqno(orderProduct.getOrderProductSeqno());
            orderDetail.setOrderProductSeqno(orderProduct);
            orderDetailRepository.save(orderDetail);
        });

        // 첫 번째 주문 상품의 orderSeqno로 주문 정보 업데이트 (환불 상태로 변경)
        Long orderSeqno = orderProducts.get(0).getOrderProductSeqno();
        OrderInfoEntity orderInfo = orderInfoRepository.findByEmailAndOrderSeqno(email, orderSeqno);

        if (orderInfo == null) {
            throw new IllegalArgumentException("해당 주문 정보가 없습니다.");
        }

        orderInfo.setOrderStatus("환불 신청");
        orderInfoRepository.save(orderInfo);
    }
*/
}
   
