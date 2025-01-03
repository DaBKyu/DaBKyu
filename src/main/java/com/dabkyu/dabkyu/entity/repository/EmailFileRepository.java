package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.EmailEntity;
import com.dabkyu.dabkyu.entity.EmailFileEntity;

public interface EmailFileRepository extends JpaRepository<EmailFileEntity, Long>{

    // 리뷰 상세 페이지에서 첨부된 파일 목록 보기
    @Query("SELECT ef FROM emailFile ef WHERE ef.emailSeqno = :email")
    public List<EmailFileEntity> findEmailFilesByEmailSeqno(@Param("email") EmailEntity email);

}
