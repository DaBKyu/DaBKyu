package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;

public interface Category3Repository extends JpaRepository<Category3Entity, Long> {

    //카테고리3 category2Seqno로 찾기
    public List<Category3Entity> findByCategory2Seqno(Category2Entity category2Entity);

    //임시 카테고리 생성시 name으로 찾기
    public Category3Entity findByCategory3Name(String string);

    //임시 카테고리
    public Category3Entity findByIsTemporaryTrue();

}
