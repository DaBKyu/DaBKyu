package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberEntity;

public interface MasterRepository extends JpaRepository<MemberEntity, String> {

    public Page<MemberEntity> findByEmailContainingOrUsernameContaining
            (String keyword1,String keyword2,Pageable pageable);

    public MemberEntity findByEmail(String email);

    

}
