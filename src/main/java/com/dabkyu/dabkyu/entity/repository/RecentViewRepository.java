package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.RecentViewEntity;
import com.dabkyu.dabkyu.entity.RecentViewEntityID;

public interface RecentViewRepository extends JpaRepository<RecentViewEntity,RecentViewEntityID>{

}
