package com.dabkyu.dabkyu.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberEntity;

import jakarta.transaction.Transactional;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

    public Optional<MemberEntity> findByUsernameAndTelno(String username, String telno);
    
    // 패스워드 변경 30일 이후로 연기
    @Transactional
    @Modifying
    @Query(value="update jpa_member set pwcheck = "
                + "(select nvl(pwcheck,0) from jpa_member "
                + "where email = :email) + 1 where email =:email",
                nativeQuery = true)
    public void modifyPasswordAfter30(@Param("email") String email);
}
