package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface ReviewRepository  extends JpaRepository<ReviewEntity, Long>{

    // 전체 리뷰 목록 보기
    Page<ReviewEntity> findByEmailContainingOrRevContentContaining(String keyword, String keyword2, PageRequest pageRequest);

    // 상품 이전 보기
	@Query("select max(r.reviewSeqno) from review r " 
    + "where r.reviewSeqno < :reviewSeqno and "
    + "(r.email like %:keyword1% or r.revContent like $:keyword2%)")
    public Long pre_seqno(@Param("reviewSeqno") Long reviewSeqno, 
        @Param("keyword1") String keyword1,
        @Param("keyword2") String keyword2);

    // 상품 다음 보기
    @Query("select min(r.reviewSeqno) from review r "
        + "where r.reviewSeqno < :reviewSeqno and "
        + "r.email like %:keyword1% or r.revContent like $:keyword2%)")
    public Long next_seqno(@Param("reviewSeqno") Long reviewSeqno, 
        @Param("keyword1") String keyword1,
        @Param("keyword2") String keyword2);

    // max seqno 구하기 --> Native SQL 
	@Query(value="select max(reviewSeqno) from review where email = :email", 
			nativeQuery=true)
	public Long getMaxSeqno(@Param("email") String email);

}
