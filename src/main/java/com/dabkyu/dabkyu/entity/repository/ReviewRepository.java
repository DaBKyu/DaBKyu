package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
//
    public Page<ReviewEntity> findByReviewSeqnoOrProductSeqno_ProductSeqno
            (Long keyword1,Long keyword2,Pageable pageable);

}
