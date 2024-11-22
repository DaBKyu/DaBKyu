package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.EmailLikeListEntity;
import com.dabkyu.dabkyu.entity.EmailLikeListEntityID;

public interface EmailLikeListRepository extends JpaRepository<EmailLikeListEntity, EmailLikeListEntityID> {

}
