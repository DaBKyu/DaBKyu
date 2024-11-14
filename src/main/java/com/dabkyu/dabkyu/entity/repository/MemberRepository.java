package com.dabkyu.dabkyu.entity.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query(value="update member set pwcheck = "
                + "(select nvl(pwcheck,0) from member "
                + "where email = :email) + 1 where email =:email",
                nativeQuery = true)
    public void modifyPasswordAfter30(@Param("email") String email);

    public Page<MemberEntity> findByEmailContainingOrUsernameContaining
            (String keyword1,String keyword2,Pageable pageable);
    
    //이메일로 사용자 정보 불러오기
    public Optional<MemberEntity> findByEmail(String email);

    //이메일로 사용자 정보 삭제하기
    public void deleteByEmail(String email);
}
