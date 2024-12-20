package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;

public interface Category2Repository extends JpaRepository<Category2Entity, Long> {

    //Category1Seqno에 해당하는 Category2 목록을 조회하는 메서드
    public List<Category2Entity> findByCategory1Seqno(Category1Entity category1Seqno);

    public Optional<Category2Entity> findByCategory2OrderAndCategory1Seqno(int category2Order, Category1Entity category1Seqno);
}
