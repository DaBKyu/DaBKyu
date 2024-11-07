package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import java.util.List;


public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    
    public List<QuestionEntity> findByEmail(MemberEntity member);

    // 내 문의 조회(페이징)
    public Page<QuestionEntity> findByEmail(MemberEntity member, Pageable pageable);
}
