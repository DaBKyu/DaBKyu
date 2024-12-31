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
import com.dabkyu.dabkyu.entity.CouponEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntity;
import com.dabkyu.dabkyu.entity.MemberCouponEntityID;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;
import com.dabkyu.dabkyu.entity.OrderProductEntity;
import com.dabkyu.dabkyu.entity.OrderProductOptionEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;
import com.dabkyu.dabkyu.entity.RelatedProductEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntity;
import com.dabkyu.dabkyu.entity.ShoppingCartEntityID;
import com.dabkyu.dabkyu.entity.repository.AddedRelatedProductRepository;
import com.dabkyu.dabkyu.entity.repository.CouponRepository;
import com.dabkyu.dabkyu.entity.repository.MemberCouponRepository;
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
import lombok.extern.log4j.Log4j2;

@Service 
@AllArgsConstructor
@Log4j2
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
    private final MemberCouponRepository memberCouponRepository;
    
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
        log.info("addToCart 서비스 호출");
        // OrderProductEntity 생성 및 저장
        log.info("productSeqno: ({})", orderProduct.getProductSeqno());
        OrderProductEntity orderProductEntity = orderProduct.dtoToEntity(orderProduct, productRepository.findById(orderProduct.getProductSeqno()).get());
        orderProductRepository.save(orderProductEntity);
        log.info("orderProduct 저장");

        // 주문 추가 상품과 주문 추가 옵션이 있는지 확인후 저장
        if (relatedProducts != null) {   
            if(!relatedProducts.isEmpty()){
                log.info("추가상품 추가 시작");
                for (Map.Entry<Long,Integer> entry : relatedProducts.entrySet()) {
                    RelatedProductEntity relatedProductEntity = relatedProductRepository.findById(entry.getKey()).get();
                    // OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProduct.getOrderProductSeqno()).get();
                    AddedRelatedProductEntity addedRelatedProductEntity = AddedRelatedProductEntity.builder()
                    .orderProductSeqno(orderProductEntity)
                    .relatedProductSeqno(relatedProductEntity)
                    .amount(entry.getValue())
                    .build();
                    addedRelatedProductRepository.save(addedRelatedProductEntity);                                                                              
                }
            }
        }

        if (productOptions != null) {
            if(!productOptions.isEmpty()){
                log.info("옵션 추가 시작");
                for (Long seqno:productOptions) {
                    ProductOptionEntity productOptionEntity = productOptionRepository.findById(seqno).get();
                    // OrderProductEntity orderProductEntity = orderProductRepository.findById(orderProduct.getOrderProductSeqno()).get();
                    OrderProductOptionEntity orderProductOptionEntity = OrderProductOptionEntity.builder()
                    .orderProductSeqno(orderProductEntity)
                    .optionSeqno(productOptionEntity)
                    .build();
                    orderProductOptionRepository.save(orderProductOptionEntity);                                                                              
                }
            }
        }

        MemberEntity memberEntity = memberRepository.findById(memberDTO.getEmail()).get();
        ShoppingCartEntity shoppingCartEntity = ShoppingCartEntity.builder()
                                                                  .email(memberEntity)
                                                                  .orderProductSeqno(orderProductEntity)
                                                                  .build();
        shoppingCartRepository.save(shoppingCartEntity);
    }


    // 장바구니에 등록된 상품 수량 수정
    @Override
    public void updateCartItemQuantity(String email, Long orderProductSeqno, int newQuantity) {
        // 장바구니 항목 가져오기
        ShoppingCartEntityID shoppingCartEntityID = new ShoppingCartEntityID(email, orderProductSeqno);
        ShoppingCartEntity cartItem = shoppingCartRepository.findById(shoppingCartEntityID).get();
        
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
    public void pay(String email, List<Long> toPayOrderProductList,Long couponSeqno, int point, OrderInfoDTO orderInfo) {

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
        MemberEntity member = memberRepository.findById(email).get();

        //적립금 적립, 사용 확인 및 적용

        // 총 가격에서 적립할 금액 구하기(적립은 총 가격에서 포인트, 배송비,쿠폰 할인금액 제한 금액(결제금액)에만 적용)
        Map<String, Object> calculateResult = calculatePrice(toPayOrderProductList, point, couponSeqno, email);
        int totalPrice = (int) calculateResult.get("totalPrice");
        int totalShippingFee = (int) calculateResult.get("totalShippingFee");
        int couponDiscount = (int) calculateResult.get("couponDiscount");
        //적립할 금액(포인트,배송,쿠폰할인금액 제한 금액)
        int amountAfterDiscounts = (int) calculateResult.get("amountAfterDiscounts");
      
        // 쿠폰 사용 확인 및 적용
        if (couponSeqno != null) {
            MemberCouponEntity memberCoupon = memberCouponRepository.findById(new MemberCouponEntityID(email, couponSeqno))
                    .orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));
            // 쿠폰 만료처리
            //CouponEntity coupon = couponRepository.findById(couponSeqno).get();
            //coupon.setIsExpire("Y");
            memberCoupon.setIsExpire("Y");
        }

        // 포인트 적립 금액 계산
        int rewardPoints = 0;
        if ("Platinum".equals(member.getMemberGrade())) {
            rewardPoints = (int) (amountAfterDiscounts * 0.05); // Platinum 등급은 5% 적립
        } else if ("Gold".equals(member.getMemberGrade())) {
            rewardPoints = (int) (amountAfterDiscounts * 0.03); // Gold 등급은 3% 적립
        } else if ("Silver".equals(member.getMemberGrade())) {
            rewardPoints = (int) (amountAfterDiscounts * 0.02); // Silver 등급은 2% 적립
        } else if ("Bronze".equals(member.getMemberGrade())) {
            rewardPoints = (int) (amountAfterDiscounts * 0.01); // Bronze 등급은 1% 적립
        }

        // 포인트 사용 및 적립 적용 (point가 null이 아닐 때만 차감)
        if (point != 0) {
            member.setPoint(member.getPoint() - point + rewardPoints);
        } else {
            member.setPoint(member.getPoint() + rewardPoints);
        }

        // 회원 정보 저장
        memberRepository.save(member);
    
      
        // 주문 정보 생성
        OrderInfoEntity orderInfoEntity = OrderInfoEntity.builder()
                                                          .email(member)
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
 // 총 가격 계산 메서드
 private Map<String,Object> calculatePrice(List<Long> toPayOrderProductList,int point, Long couponSeqno, String email) {
    int totalPrice = 0;
    String isFree = "Y";
    int totalShippingFee = 0;
    //int totalDiscount = 0;
    int couponDiscount = 0;

    if (couponSeqno != null) {
    // 쿠폰 정보를 DB에서 가져오고, 할인 금액을 계산하는 로직을 추가
    MemberEntity member = memberRepository.findById(email).get();
    MemberCouponEntity memberCoupon = memberCouponRepository.findById(new MemberCouponEntityID(member.getEmail(), couponSeqno)) 
            .orElseThrow(() -> new RuntimeException("유효한 쿠폰이 아닙니다."));

    CouponEntity coupon = memberCoupon.getCouponSeqno();

     // 최소 주문 금액(minOrder) 확인
     int minOrder = coupon.getMinOrder();
     if (totalPrice >= minOrder) {
         // 퍼센트 할인과 금액 할인 조건에 맞는 계산 처리
         if (coupon.getPercentDiscount() > 0) {
             // 퍼센트 할인 적용 (최대 할인 금액 고려)
             int percentDiscountAmount = totalPrice * coupon.getPercentDiscount() / 100;
             couponDiscount = Math.min(percentDiscountAmount, coupon.getAmountDiscount());
         } else if (coupon.getAmountDiscount() > 0) {
             // 금액 할인 적용
             couponDiscount = coupon.getAmountDiscount();
         }
     }
 }


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

    // 배송비 계산
    if ("N".equals(isFree)) {
        totalShippingFee = 3000;
    }

    // 총 금액에서 포인트와 쿠폰 할인액 차감
    int amountAfterDiscounts = totalPrice + totalShippingFee - couponDiscount - point;

    Map<String, Object> priceInfo = new HashMap<>();
    priceInfo.put("totalPrice", totalPrice);
    priceInfo.put("totalShippingFee", totalShippingFee);
    priceInfo.put("couponDiscount", couponDiscount);
    priceInfo.put("amountAfterDiscounts", amountAfterDiscounts);
    //priceInfo.put("isFree", isFree);

    return priceInfo;
}

   // 결제 완료된 제품만 장바구니에서 삭제
   @Transactional
   @Override
   public void removeProduct(String email) {
   List<OrderInfoEntity> orderInfoList = orderInfoRepository.findByEmail_Email(email);

    for (OrderInfoEntity orderInfo : orderInfoList) {
        // 주문 상태가 "결제 완료"
        if ("결제 완료".equals(orderInfo.getOrderStatus())) {
            
            // 해당 주문에 포함된 상품들을 찾기
            List<OrderDetailEntity> orderDetails = orderDetailRepository.findByOrderSeqno(orderInfo);
            
            for (OrderDetailEntity orderDetail : orderDetails) {
                //OrderProductEntity orderProduct = orderDetail.getOrderProductSeqno();
                //orderProductRepository.delete(orderProduct);
                orderDetailRepository.delete(orderDetail);
                }
            }
        }
    }      

    //결제 취소 신청
    public void cancelToPay(String email, Long orderSeqno) {
        
        // 주문 정보 조회
        OrderInfoEntity orderInfo = orderInfoRepository.findById(orderSeqno).get();

        // 주문자가 맞는지 확인
        if (!orderInfo.getEmail().getEmail().equals(email)) {
            throw new RuntimeException("이 주문은 해당 회원이 요청할 수 없습니다.");
        }

        // 주문 상태 변경 (결제 취소)
        orderInfo.setOrderStatus("취소 신청");
        orderInfoRepository.save(orderInfo);
    }

    // 환불 신청
    public void refundRequest(String email, Long orderSeqno) {
        // 주문 상품 조회
       OrderInfoEntity orderInfo = orderInfoRepository.findByEmail_EmailAndOrderSeqno(email, orderSeqno);

        // 주문자가 맞는지 확인
        if (!orderInfo.getEmail().getEmail().equals(email)) {
        throw new RuntimeException("이 주문은 해당 회원이 요청할 수 없습니다.");
        }

       orderInfo.setOrderStatus("환불 신청");
       orderInfoRepository.save(orderInfo);
    }

}
   
