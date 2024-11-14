package com.dabkyu.dabkyu.entity.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dabkyu.dabkyu.entity.LikeListEntity;
import com.dabkyu.dabkyu.entity.LikeListEntityID;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;

public interface LikeListRepository extends JpaRepository<LikeListEntity,LikeListEntityID> {
    
    // 회원이 찜한 상품 날짜 내림차순 조회
    @Query("SELECT l.productSeqno FROM likeList l " +
                  "WHERE l.email=:email " +
                  "ORDER BY l.likeDate DESC")
    public Page<ProductEntity> findLikedProductByEmail(
        @Param("email") MemberEntity member,
        Pageable pageable
    );

    public List<LikeListEntity> findByProductSeqno_ProductSeqno(Long productSeqno);
}
