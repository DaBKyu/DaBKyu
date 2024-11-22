package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface ReviewRepository  extends JpaRepository<ReviewEntity, Long> {
  
  public Page<ReviewEntity> findByReviewSeqnoOrProductSeqno_ProductSeqno
            (Long keyword1,Long keyword2,Pageable pageable);

  // 전체 리뷰 목록 보기
  public Page<ReviewEntity> findByEmail_EmailContainingOrRevContentContaining(String keyword1, String keyword2, PageRequest pageRequest);

  // 상품 이전 보기
	@Query("select max(r.reviewSeqno) from review r " 
    + "where r.reviewSeqno < :reviewSeqno and "
    + "(r.email.email like %:keyword1% or r.revContent like %:keyword2%)")
  public Long pre_seqno(@Param("reviewSeqno") Long reviewSeqno, 
        @Param("keyword1") String keyword1,
        @Param("keyword2") String keyword2);

  // 상품 다음 보기
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
  //ReviewRepository에서 카테고리별 리뷰 조회 쿼리
  @Query("SELECT r FROM review r WHERE r.productSeqno.category3Seqno.category2Seqno.category1Seqno = :category")
  public Page<ReviewEntity> findByCategory(@Param("category") Long category, Pageable pageable);

}
