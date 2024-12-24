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

    //같은 부모(Category1Entity) 내에서 대상 순서를 가진 항목을 조회
    @Query("SELECT c2 FROM category2 c2 WHERE c2.category2Order = :category2Order AND c2.category1Seqno.category1Seqno = :category1Seqno")
    public Optional<Category2Entity> findByCategory2OrderAndCategory1Seqno(int category2Order, Long category1Seqno);

    //카테고리2 순서 구하기
    @Query("SELECT MAX(c.category2Order) FROM category2 c WHERE c.category1Seqno.category1Seqno = :category1Seqno")
    public Integer findMaxCategory2OrderByCategory1(@Param("category1Seqno") Long category1Seqno);

    //동일한 Category1Seqno를 가진 Category2 항목들을 조회
    @Query("SELECT c2 FROM category2 c2 WHERE c2.category1Seqno.category1Seqno = :category1Seqno")
    public List<Category2Entity> findCategory2ByCategory1Seqno(@Param("category1Seqno") Long category1Seqno);


}
