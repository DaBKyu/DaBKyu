package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberNotificationEntity;
import com.dabkyu.dabkyu.entity.MemberNotificationEntityID;

public interface MemberNotificationRepository extends JpaRepository<MemberNotificationEntity, MemberNotificationEntityID>{


}
