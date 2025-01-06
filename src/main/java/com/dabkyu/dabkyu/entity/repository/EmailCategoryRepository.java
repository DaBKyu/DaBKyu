package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.EmailCategoryEntity;
import com.dabkyu.dabkyu.entity.EmailCategoryEntityID;

public interface EmailCategoryRepository extends JpaRepository<EmailCategoryEntity, EmailCategoryEntityID>{

}
