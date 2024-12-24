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

    //같은 부모(Category2Entity) 내에서 대상 순서를 가진 항목을 조회
    @Query("SELECT c3 FROM category3 c3 WHERE c3.category3Order = :category3Order AND c3.category2Seqno.category2Seqno = :category2Seqno")
    public Optional<Category3Entity> findByCategory3OrderAndCategory2Seqno(int category3Order, Long category2Seqno);

    //카테고리3 순서 구하기
    @Query("SELECT MAX(c.category3Order) FROM category3 c WHERE c.category2Seqno.category2Seqno = :category2Seqno")
    public Integer findMaxCategory3OrderByCategory2(@Param("category2Seqno") Long category2Seqno);

    //동일한 Category2Seqno를 가진 Category3 항목들을 조회
    @Query("SELECT c3 FROM category3 c3 WHERE c3.category2Seqno.category2Seqno = :category2Seqno")
    public List<Category3Entity> findCategory3ByCategory2Seqno(@Param("category2Seqno") Long category2Seqno);

}
