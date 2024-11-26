package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.Category3Entity;

public interface Category3Repository extends JpaRepository<Category3Entity, Long> {

    //임시 카테고리 "Y"인 카테고리 검색
    public Category3Entity findByIsTemporary(String isTemporary);

}
