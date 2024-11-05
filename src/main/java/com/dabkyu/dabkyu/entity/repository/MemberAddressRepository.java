package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberAddressEntity;

import java.util.List;


public interface MemberAddressRepository extends JpaRepository<MemberAddressEntity, Long> {
    public List<MemberAddressEntity> findByEmail(String email);
}
