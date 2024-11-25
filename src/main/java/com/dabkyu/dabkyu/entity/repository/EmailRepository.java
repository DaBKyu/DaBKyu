
package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.EmailEntity;

public interface EmailRepository extends JpaRepository<EmailEntity,Long>{

     // maxSeqno 찾기
     @Query("SELECT MAX(e.emailSeqno) FROM email e")
     public Long findMaxSeqno();

     // 메일발송내역 조회
     public Page<EmailEntity> findByEmailTitleContainingOrEmailContentContaining(String keyword1, String keyword2, PageRequest pageRequest);

     // 상품 이전 보기
	@Query("select max(e.emailSeqno) from email e " 
            + "where e.emailSeqno < :emailSeqno and "
            + "(e.emailTitle like %:keyword1% or e.emailContent like %:keyword2%)")
     public Long pre_seqno(@Param("emailSeqno") Long emailSeqno, 
        @Param("keyword1") String keyword1,
        @Param("keyword2") String keyword2);

     // 상품 다음 보기
     @Query("select min(e.emailSeqno) from email e "
          + "where e.emailSeqno < :emailSeqno and "
          + "(e.emailTitle like %:keyword1% or e.emailContent like %:keyword2%)")
     public Long next_seqno(@Param("emailSeqno") Long reviewSeqno, 
          @Param("keyword1") String keyword1,
          @Param("keyword2") String keyword2);
    
}
