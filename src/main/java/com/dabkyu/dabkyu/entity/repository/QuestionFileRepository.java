package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.QuestionFileEntity;

public interface QuestionFileRepository extends JpaRepository<QuestionFileEntity, Long> {

    // 사용자 이메일로 문의 검색
    @Query("SELECT qf FROM QUESTIONFILE qf "
    + "JOIN qf.queSeqno q "
    + "WHERE q.email.email=:email")
    public List<QuestionFileEntity> findByEmail(@Param("email") String email);

    public List<QuestionFileEntity> findByQueSeqno(Long queFileSeqno);
    
}
