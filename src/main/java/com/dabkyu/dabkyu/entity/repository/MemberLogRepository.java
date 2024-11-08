package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberLogEntity;
import com.dabkyu.dabkyu.entity.MemberLogEntityID;

public interface MemberLogRepository extends JpaRepository<MemberLogEntity, MemberLogEntityID> {

}
