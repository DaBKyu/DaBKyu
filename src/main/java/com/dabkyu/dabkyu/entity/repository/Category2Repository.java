package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;

public interface Category2Repository extends JpaRepository<Category2Entity, Long>{

    public Category2Entity findByCategory2Seqno(Long category2Seqno);

}
