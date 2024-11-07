package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.OrderInfoEntity;

public interface OrderInfoRepository extends JpaRepository<OrderInfoEntity, Long> {

    List<OrderInfoEntity> findByEmail(String email);

 }
