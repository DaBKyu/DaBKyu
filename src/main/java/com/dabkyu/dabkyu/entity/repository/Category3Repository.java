package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.Category3Entity;

public interface Category3Repository extends JpaRepository<Category3Entity, Long> {

    //임시 카테고리
    public Category3Entity findByIsTemporaryTrue();

}
