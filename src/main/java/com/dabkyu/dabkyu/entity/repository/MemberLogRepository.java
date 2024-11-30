package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberLogEntity;
import com.dabkyu.dabkyu.entity.MemberLogEntityID;

public interface MemberLogRepository extends JpaRepository<MemberLogEntity, MemberLogEntityID> {

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


}
