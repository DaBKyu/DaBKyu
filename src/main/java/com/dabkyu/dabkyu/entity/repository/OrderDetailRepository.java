package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.dto.CategorySalesDTO;
import com.dabkyu.dabkyu.dto.ProductSalesDTO;
import com.dabkyu.dabkyu.entity.OrderDetailEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    // 특정 주문에 해당하는 모든 주문 상세 정보 조회
    public List<OrderDetailEntity> findByOrderSeqno_OrderSeqno(OrderInfoEntity orderSeqno);

    // 특정 주문 상품에 해당하는 주문 상세 정보 조회
    public OrderDetailEntity findByOrderSeqno_OrderSeqno(Long orderSeqno);

    //public Page<OrderDetailEntity> findAll(PageRequest pageRequest);

    @Query("SELECT od FROM orderDetail od " +
       "JOIN od.orderProductSeqno op " +
       "JOIN op.productSeqno p " +
       "WHERE p.category3Seqno.category2Seqno.category1Seqno = :category " +
       "AND p.productName LIKE %:productName%")
    public Page<OrderDetailEntity> findByCategoryAndProductNameContaining(@Param("category") Long category, 
                                                                        @Param("productName") String productName,
                                                                        Pageable pageable);

    @Query("SELECT od FROM orderDetail od " +
        "JOIN od.orderProductSeqno op " +
        "JOIN op.productSeqno p " +
        "WHERE p.category3Seqno.category2Seqno.category1Seqno = :category")
    public Page<OrderDetailEntity> findByCategory(@Param("category") Long category, Pageable pageable);

    public Page<OrderDetailEntity> findByOrderProductSeqno_ProductSeqno_ProductNameContaining(String productName, Pageable pageable);

    // 카테고리별 매출액 구하기 
    @Query("SELECT new com.dabkyu.dabkyu.dto.CategorySalesDTO(p.category3Seqno.category3Name, SUM(oi.totalPrice)) " +
            "FROM orderDetail od " +
            "JOIN od.orderProductSeqno op " +
            "JOIN op.productSeqno p " +
            "JOIN od.orderSeqno oi " +
            "WHERE od.cancelYn = 'N' AND od.refundYn = 'N' " +
            "GROUP BY p.category3Seqno.category3Name")
    public List<CategorySalesDTO> getCategorySales();

    //상품별 매출액
    @Query(value = "SELECT pd.product_name AS productName,SUM(op.amount * pd.price) AS totalSales " +
    "FROM order_detail od, order_product op, product pd " +
    "WHERE od.orderproduct_seqno = op.orderproduct_seqno " +
    "AND op.product_seqno = pd.product_seqno " +
    "AND od.refund_yn = 'N' " +
    "AND od.cancel_yn = 'N' " + 
    "GROUP BY pd.product_name",
       nativeQuery = true)
    public List<Object[]> findProductSales(); // Object[] 형태로 반환

    @Query("SELECT od FROM orderDetail od " +
           "JOIN FETCH od.orderSeqno os " +
           "JOIN FETCH od.orderProductSeqno op " +
           "JOIN FETCH op.productSeqno p")
    List<OrderDetailEntity> findAllWithDetails();

}
