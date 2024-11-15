package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ReportEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    //리뷰 신고 리스트
    public Page<ReportEntity> findByReportTitleContaining(String keyword, Pageable pageable);

}
