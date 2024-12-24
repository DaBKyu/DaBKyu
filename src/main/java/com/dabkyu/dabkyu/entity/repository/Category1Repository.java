package com.dabkyu.dabkyu.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dabkyu.dabkyu.entity.Category1Entity;

public interface Category1Repository extends JpaRepository<Category1Entity, Long> {

    public Optional<Category1Entity> findByCategory1Order(int category1Order);

    @Query("SELECT MAX(c.category1Order) FROM category1 c")
    public Integer findMaxCategory1Order();


}
