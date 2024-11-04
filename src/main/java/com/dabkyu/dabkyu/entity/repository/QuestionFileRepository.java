package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.QuestionFileEntity;

public interface QuestionFileRepository extends JpaRepository<QuestionFileEntity, Long> {

    public List<QuestionFileEntity> findByQueSeqno(Long queseqno);
}
