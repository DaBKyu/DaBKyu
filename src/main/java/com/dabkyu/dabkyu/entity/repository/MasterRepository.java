package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Streamable;

import com.dabkyu.dabkyu.entity.MemberEntity;

public interface MasterRepository extends JpaRepository<MemberEntity, Long> {

    public Page<MemberEntity> findByEmailContainingOrUsernameContaing
            (String keyword1,String keyword2,Pageable pageable);

    public MemberEntity findByEmail(String email);

    

}
