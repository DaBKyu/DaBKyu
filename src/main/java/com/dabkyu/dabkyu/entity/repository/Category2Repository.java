package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;

public interface Category2Repository extends JpaRepository<Category2Entity, Long>{

    //카테고리2 category1Seqno로 찾기
    public List<Category2Entity> findByCategory1Seqno(Category1Entity category1Entity);
}
