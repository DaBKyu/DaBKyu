package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    Page<AddressEntity> findByRoadContainingOrBuildingContaining(
        String addrSearch1, String addrSearch2, Pageable pageable
    );
}
