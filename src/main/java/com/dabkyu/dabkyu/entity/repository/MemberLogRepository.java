package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberLogEntity;
import com.dabkyu.dabkyu.entity.MemberLogEntityID;

public interface MemberLogRepository extends JpaRepository<MemberLogEntity, MemberLogEntityID> {

    //일별 방문자 통계
    @Query(value = "SELECT TRUNC(m.inouttime) AS VISIT_DATE, COUNT(*) AS VISITOR_COUNT " +
    "FROM member_log m " +
    "WHERE m.inouttime >= :startDate AND m.inouttime <= :endDate " +
    "AND m.status = 'login' " + 
    "GROUP BY TRUNC(m.inouttime) " +
    "ORDER BY TRUNC(m.inouttime) ASC", 
    nativeQuery = true)
    public List<Object[]> findDailyVisitorsWithinDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    //관리자페이지 메인 방문자수 
    @Query(value = "SELECT COUNT(*) AS VISITOR_COUNT " +
               "FROM member_log m " +
               "WHERE TRUNC(m.inouttime) = TRUNC(:referenceDate) " + // 날짜 부분만 비교
               "AND m.status = 'login'",
       nativeQuery = true)
    public int getVisitorCount(
        @Param("referenceDate") LocalDateTime referenceDate
    );



}
