package com.dabkyu.dabkyu.entity.repository;

import java.time.LocalDateTime;
import java.util.List;
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

    Page<MemberEntity> findByMemberGrade(String memberGrade, Pageable pageable); // 회원등급으로 검색

    
    // 패스워드 변경 30일 이후로 연기
    @Transactional
    @Modifying
    @Query(value="update member set pwcheck = "
                + "(select nvl(pwcheck,0) from member "
                + "where email = :email) + 1 where email =:email",
                nativeQuery = true)
    public void modifyPasswordAfter30(@Param("email") String email);

    //회원 등급/이름으로 검색
    public Page<MemberEntity> findByMemberGradeOrUsernameContaining
                (String keyword1,String keyword2,Pageable pageable);
    
    public Page<MemberEntity> findByEmailContainingOrUsernameContaining(
        String keyword1,String keyword2,Pageable pageable
    );

    //이메일 수신 동의 멤버 리스트
    @Query(value="select m from member m where m.emailRecept = 'Y'")
    public List<MemberEntity> findByEmailRecept();
}
