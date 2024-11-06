package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

public interface ReviewFileRepository extends JpaRepository<ReviewFileEntity, Long> {

    // 사용자 이메일로 리뷰 검색
    @Query("SELECT rf FROM REVIEWFILE rf "
                + "JOIN rf.reviewSeqno r "
                + "WHERE r.email.email=:email")
    public List<ReviewFileEntity> findByEmail(@Param("email") String email);

    public List<ReviewFileEntity> findByReviewSeqno(ReviewEntity review);
}
