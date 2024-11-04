package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

public interface ReviewFileRepository extends JpaRepository<ReviewFileEntity, Long> {

    public List<ReviewFileEntity> findByReviewSeqno(ReviewEntity reviewSeqno);
}
