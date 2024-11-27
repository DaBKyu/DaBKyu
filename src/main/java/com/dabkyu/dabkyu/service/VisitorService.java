package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.List;

import com.dabkyu.dabkyu.dto.VisitorCountDTO;

public interface VisitorService {

    //고유 방문자 기록
    public void logVisitor(String ipAddress);

    //오늘의 고유 방문자 수 조회
    public Long getTodayVisitorCount();

    // 날짜별 방문자 수 조회
    public List<VisitorCountDTO> getVisitorCountByDate(LocalDateTime start, LocalDateTime end);

}
