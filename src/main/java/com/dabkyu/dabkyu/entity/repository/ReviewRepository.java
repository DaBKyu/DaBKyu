package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface ReviewRepository  extends JpaRepository<ReviewEntity, Long> {
  
  // 해당 제품에 대한 리뷰 보기
  public Page<ReviewEntity> findByProductSeqno_ProductSeqno(Long productSeqno, Pageable pageable);

  // 리뷰 이전 보기
	@Query("select max(r.reviewSeqno) from review r " 
    + "where r.reviewSeqno < :reviewSeqno and "
    + "(r.email.email like %:keyword1% or r.revContent like %:keyword2%)")
  public Long pre_seqno(@Param("reviewSeqno") Long reviewSeqno, 
        @Param("keyword1") String keyword1,
        @Param("keyword2") String keyword2);

  // 리뷰 다음 보기
  @Query("select min(r.reviewSeqno) from review r "
      + "where r.reviewSeqno < :reviewSeqno and "
      + "(r.email.email like %:keyword1% or r.revContent like %:keyword2%)")
  public Long next_seqno(@Param("reviewSeqno") Long reviewSeqno, 
        @Param("keyword1") String keyword1,
        @Param("keyword2") String keyword2);

  // max seqno 구하기 --> Native SQL 
	@Query(value="select max(reviewSeqno) from review where email = :email", 
			nativeQuery=true)
	public Long getMaxSeqno(@Param("email") String email);
  
  public Page<ReviewEntity> findByEmail(MemberEntity email, Pageable pageable);

  //관리자페이지 리뷰 카테고리
  @Query("SELECT r FROM review r " +
           "JOIN r.productSeqno p " +              
           "JOIN p.category3Seqno c3 " +           
           "WHERE c3.category3Seqno = :category3Seqno")
  Page<ReviewEntity> findByCategory(@Param("category") Long category, Pageable pageable);

  //관리자페이지 메인 오늘 리뷰수 
  @Query(value = "SELECT COUNT(*) AS REVIEW_COUNT " +
       "FROM review r " +
       "WHERE TRUNC(r.rev_date) = TRUNC(:referenceDate)", 
nativeQuery = true)
public int getReviewCount(@Param("referenceDate") LocalDateTime referenceDate);




 


}
