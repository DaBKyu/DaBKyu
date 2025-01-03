package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ReportEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    //리뷰 신고 리스트
    public Page<ReportEntity> findByReportTitleContaining(String keyword, Pageable pageable);

    @Query("SELECT r FROM report r WHERE r.reviewSeqno = :review")
    public List<ReportEntity> findByReviewSeqno(@Param("review") ReviewEntity review);

     //reviewEntity로 report 찾기
    @Query("SELECT r FROM report r WHERE r.reviewSeqno =:reviewEntity")
    public List<ReportEntity> findReportByReviewSeqno(@Param("reviewEntity") ReviewEntity reviewEntity);
}
