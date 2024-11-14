package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.Category1Entity;

public interface Category1Repository extends JpaRepository<Category1Entity, Long>{

    public Category1Entity findByCategory1Seqno(Long category1Seqno);

}
