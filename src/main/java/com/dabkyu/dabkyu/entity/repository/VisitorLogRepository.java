package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dabkyu.dabkyu.entity.VisitorLogEntity;

public interface VisitorLogRepository extends JpaRepository<VisitorLogEntity,Long> {

    // IP 주소와 날짜 범위가 주어진 조건에 맞는 방문 로그가 존재하는지 확인하는 메서드
    public boolean existsByIpAddressAndVisitDateBetween(String ipAddress, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // 주어진 날짜 범위에 대한 방문 로그의 수를 세는 메서드
    public Long countByVisitDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);

    // 날짜별 방문자 수를 구하는 쿼리
    @Query("SELECT v.visitDate.toLocalDate(), COUNT(v) " +
           "FROM visitorLog v " +
           "WHERE v.visitDate BETWEEN :start AND :end " +
           "GROUP BY v.visitDate.toLocalDate()")
    public List<Object[]> countVisitorCountByDate(LocalDateTime start, LocalDateTime end);



                                      



}
