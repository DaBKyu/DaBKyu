package com.dabkyu.dabkyu.entity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;

public interface Category3Repository extends JpaRepository<Category3Entity, Long> {

    //임시 카테고리 "Y"인 카테고리 검색
    public Category3Entity findByIsTemporary(String isTemporary);

    //Category2Seqno에 해당하는 Category3 목록을 조회하는 메서드
    public List<Category3Entity> findByCategory2Seqno(Category2Entity category2Seqno);

    //Category3 찾기
    public Category3Entity findByCategory3Seqno(Long category3Seqno);

    public Optional<Category3Entity> findByCategory3OrderAndCategory2Seqno(int category3Order, Category2Entity category2Seqno);

}
