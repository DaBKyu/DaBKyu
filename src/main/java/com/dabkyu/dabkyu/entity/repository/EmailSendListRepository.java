package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.EmailSendListEntity;

public interface EmailSendListRepository extends JpaRepository<EmailSendListEntity,Long>{

     // 모든 메일 발송 내역을 페이지네이션을 사용하여 조회
     Page<EmailSendListEntity> findAll(Pageable pageable);
    



}
