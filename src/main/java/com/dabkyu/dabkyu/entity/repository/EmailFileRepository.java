package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.EmailEntity;
import com.dabkyu.dabkyu.entity.EmailFileEntity;

public interface EmailFileRepository extends JpaRepository<EmailFileEntity, Long>{

    // 리뷰 상세 페이지에서 첨부된 파일 목록 보기
    public List<EmailFileEntity> findByEmailSeqno(EmailEntity email);
}
