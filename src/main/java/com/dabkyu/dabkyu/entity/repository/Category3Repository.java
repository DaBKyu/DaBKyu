package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.Category3Entity;

public interface Category3Repository extends JpaRepository<Category3Entity, Long> {

   //카테고리 조회
   public Category3Entity findByCategory3Seqno(Long category3Seqno);

}
