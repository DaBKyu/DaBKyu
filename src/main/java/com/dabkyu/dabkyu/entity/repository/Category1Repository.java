package com.dabkyu.dabkyu.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.Category1Entity;

public interface Category1Repository extends JpaRepository<Category1Entity, Long> {

    public Optional<Category1Entity> findByCategory1Order(int category1Order);

}
