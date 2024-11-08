package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.QuestionEntity;
import java.util.List;


public interface QuestionRepository extends JpaRepository<QuestionEntity, Long> {
    
    public List<QuestionEntity> findByEmail(MemberEntity member);

    //문의 목록 보기
    public Page<QuestionEntity> findByQueTitleContainingOrQueWriterContainingOrQueContentContaining(
        String keyword1, String keyword2, String keyword3, Pageable pageable);

    //게시물 이전 보기 --> JPQL
	@Query("select max(q.queSeqno) from question q " 
    + "where q.queSeqno < :queSeqno and "
    + "(q.queWriter like %:keyword1% or q.queTitle like %:keyword2% or "
    + "queContent like %:keyword3%)")
    public Long pre_seqno(@Param("queSeqno") Long queSeqno, 
        @Param("keyword1") String keyword1, 
        @Param("keyword2") String keyword2, 
        @Param("keyword3") String keyword3);

    //게시물 다음 보기 --> JPQL
    @Query("select min(q.queSeqno) from question q "
        + "where q.queSeqno > :queSeqno and (q.queWriter like %:keyword1% or "
        + "q.queTitle like %:keyword2% or queContent like %:keyword3%)")
    public Long next_seqno(@Param("queSeqno") Long queSeqno, 
        @Param("keyword1") String keyword1, 
        @Param("keyword2") String keyword2, 
        @Param("keyword3") String keyword3);


    //max seqno 구하기 --> Native SQL 
	@Query(value="select max(queSeqno) from question where email = :email", 
			nativeQuery=true)
	public Long getMaxSeqno(@Param("email") String email);

    
}