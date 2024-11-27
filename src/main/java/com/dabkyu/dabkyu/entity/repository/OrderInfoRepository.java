package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.OrderInfoEntity;

public interface OrderInfoRepository extends JpaRepository<OrderInfoEntity, Long> {

    public List<OrderInfoEntity> findByEmail_Email(String email);

    public OrderInfoEntity findByEmail_EmailAndOrderSeqno(String email, Long orderSeqno);

    public Page<OrderInfoEntity> findByOrderSeqnoOrEmail_Email
            (Long keyword1,String keyword2,Pageable pageable);
    
    public OrderInfoEntity findByOrderSeqno(OrderInfoEntity orderInfoEntity);

    public List<OrderInfoEntity> findByOrderStatusIn(List<String> statuses);
    
    public List<OrderInfoEntity> findByOrderStatus(String orderStatus);

    public List<OrderInfoEntity> findByOrderDateBefore(LocalDateTime date);

    // 구매한 상품 중 구매한지 2년된 상품이 하나라도 있는 멤버들 조회
    @Query("SELECT DISTINCT o.email FROM orderInfo o " +
    "WHERE o.email.emailRecept = 'Y' " + // 이메일 수신 동의한 멤버 필터링
    "AND o.orderDate <= :twoYearsAgo")  // twoYearsAgo 날짜 기준으로 필터링
    public List<MemberEntity> findMembersWithEmailReceptAndOrderDateOver2Years(@Param("twoYearsAgo") LocalDateTime twoYearsAgo);

    // 최근 1년내의 구매이력이 없는 멤버들 조회
    @Query("SELECT DISTINCT m FROM member m " +
       "WHERE m.emailRecept = 'Y' " + // 이메일 수신 동의한 멤버 필터링
       "AND NOT EXISTS (" +
       "    SELECT o FROM orderInfo o " +
       "    WHERE o.email = m " + // OrderInfoEntity와 MemberEntity의 관계를 매핑
       "    AND o.orderDate >= :oneYearAgo" + // 최근 1년 이내의 주문 제외
       ")")
    public List<MemberEntity> findMembersWithNoOrderInLastYear(@Param("oneYearAgo") LocalDateTime oneYearAgo);

    //일별 매출
    @Query(value = "SELECT TRUNC(o.order_date) AS ORDER_DATE_STR, SUM(o.total_price) AS TOTAL_SALES " +
                "FROM order_info o " +
                "JOIN order_detail od ON o.order_seqno = od.order_seqno " +
                "WHERE o.order_date BETWEEN :startDate AND :endDate " +
                "AND od.refund_yn = 'N' " +
                "AND od.cancel_yn = 'N' " +
                "GROUP BY TRUNC(o.order_date) " +
                "ORDER BY TRUNC(o.order_date) ASC", 
          nativeQuery = true)
    public List<Object[]> findDailySalesWithinDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    //월별 매출
    @Query(value = "SELECT TRUNC(o.order_date, 'MM') AS month, SUM(o.total_price) " +
                    "FROM order_info o " + 
                    "JOIN order_detail od ON o.order_seqno = od.order_seqno " +
                    "WHERE EXTRACT(YEAR FROM o.order_date) = :year " +
                    "AND od.refund_yn = 'N' " +
                    "AND od.cancel_yn = 'N' " +
                    "GROUP BY TRUNC(o.order_date, 'MM') " +
                    "ORDER BY TRUNC(o.order_date, 'MM')",
           nativeQuery = true)
    public List<Object[]> findMonthlySalesByYear(@Param("year") int year);

}
