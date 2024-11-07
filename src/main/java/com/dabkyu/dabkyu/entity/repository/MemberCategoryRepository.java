package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberCategoryEntity;
import com.dabkyu.dabkyu.entity.MemberCategoryEntityID;

import java.util.List;


public interface MemberCategoryRepository extends JpaRepository<MemberCategoryEntity,MemberCategoryEntityID> {

    public List<MemberCategoryEntity> findByEmail_Email(String email);

}
