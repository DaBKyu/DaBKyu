package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.QuestionCommentEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import com.dabkyu.dabkyu.entity.QuestionFileEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;
import com.dabkyu.dabkyu.entity.ReviewFileEntity;

public interface ReviewFileRepository extends JpaRepository<ReviewFileEntity, Long> {

    // 사용자 이메일로 리뷰 검색
    @Query("SELECT rf FROM reviewFile rf "
                + "JOIN rf.reviewSeqno r "
                + "WHERE r.email.email=:email")
    public List<ReviewFileEntity> findByEmail(@Param("email") String email);

    // 리뷰 상세 페이지에서 첨부된 파일 목록 보기
    public List<ReviewFileEntity> findByReviewSeqno(ReviewEntity review);

    //reviewEntity로 reviewFile찾기
    @Query("SELECT rf FROM reviewFile rf WHERE rf.reviewSeqno =:reviewEntity")
    public List<ReviewFileEntity> findReviewFileByReviewSeqno(@Param("reviewEntity") ReviewEntity reviewEntity);
    
   
}
