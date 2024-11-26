package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberCategoryEntity;
import com.dabkyu.dabkyu.entity.MemberCategoryEntityID;
import com.dabkyu.dabkyu.entity.MemberEntity;

import java.util.List;


public interface MemberCategoryRepository extends JpaRepository<MemberCategoryEntity,MemberCategoryEntityID> {

    public List<MemberCategoryEntity> findByEmail_Email(String email);

    public List<MemberCategoryEntity> findByCategory3Seqno_Category3Seqno(Long category3Seqno);

    // 여러 개의 category3Seqno를 기반으로 회원을 조회하는 쿼리
    @Query("SELECT DISTINCT mc.email " +
            "FROM memberCategory mc " +
            "WHERE mc.category3Seqno.category3Seqno IN :category3SeqnoList " +
            "AND mc.email.emailRecept = 'Y'")
    public List<MemberEntity> findMembersByCategory3SeqnoList(@Param("category3SeqnoList") List<Long> category3SeqnoList);


}
