package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.QuestionCommentEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;

public interface QuestionCommentRepository extends JpaRepository<QuestionCommentEntity, Long>{
    
    public QuestionCommentEntity findByQueSeqno(QuestionEntity queSeqno);
    public void deleteByQueSeqno(QuestionEntity queSeqno);

    //questionEntity로 questionComment찾기
    @Query("SELECT qc FROM questionComment qc WHERE qc.queSeqno =:questionEntity")
    public List<QuestionCommentEntity> findQuestionCommentByQueSeqno(@Param("questionEntity") QuestionEntity questionEntity);
   
}
