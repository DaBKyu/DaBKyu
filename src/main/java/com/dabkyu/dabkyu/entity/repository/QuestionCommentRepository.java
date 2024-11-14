package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.QuestionCommentEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;

public interface QuestionCommentRepository extends JpaRepository<QuestionCommentEntity, Long>{
    
    public QuestionCommentEntity findByQueSeqno(QuestionEntity queSeqno);
    public void deleteByQueSeqno(QuestionEntity queSeqno);
   
}
