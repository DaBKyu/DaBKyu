package com.dabkyu.dabkyu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.dto.VisitorCountDTO;
import com.dabkyu.dabkyu.entity.VisitorLogEntity;
import com.dabkyu.dabkyu.entity.repository.VisitorLogRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class VisitorServiceImpl implements VisitorService{

    private final VisitorLogRepository visitorLogRepository;

    // 오늘의 시작과 끝 시간을 계산하는 메서드
    private LocalDateTime getStartOfDay() {
        return LocalDateTime.now().toLocalDate().atStartOfDay(); // 오늘 00:00:00
    }

    private LocalDateTime getEndOfDay() {
        return LocalDateTime.now().toLocalDate().atTime(LocalTime.MAX); // 오늘 23:59:59.999999
    }

    // 고유 방문자 기록
    @Override
    public void logVisitor(String ipAddress) {
        LocalDateTime startOfDay = getStartOfDay();  // 오늘 00:00:00
        LocalDateTime endOfDay = getEndOfDay();  // 오늘 23:59:59

        // 오늘 해당 IP로 이미 방문했는지 확인
        if (!visitorLogRepository.existsByIpAddressAndVisitDateBetween(ipAddress, startOfDay, endOfDay)) {
            VisitorLogEntity visitorLog = new VisitorLogEntity();
            visitorLog.setIpAddress(ipAddress);
            visitorLog.setVisitDate(LocalDateTime.now()); // 현재 시간 저장
            visitorLogRepository.save(visitorLog); // 고유 방문자로 저장
        }
    }


    // 오늘의 고유 방문자 수 조회
    @Override
    public Long getTodayVisitorCount() {
        LocalDateTime startOfDay = getStartOfDay();  // 오늘 00:00:00
        LocalDateTime endOfDay = getEndOfDay();  // 오늘 23:59:59

        return visitorLogRepository.countByVisitDateBetween(startOfDay, endOfDay);
    }

    @Override
    public List<VisitorCountDTO> getVisitorCountByDate(LocalDateTime start, LocalDateTime end) {
        // 날짜별 방문자 수 계산
        List<Object[]> results = visitorLogRepository.countVisitorCountByDate(start, end);

        return results.stream()
                .map(result -> new VisitorCountDTO((LocalDate) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }


}
