package com.dabkyu.dabkyu.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberReviewLikeEntity;
import com.dabkyu.dabkyu.entity.MemberReviewLikeEntityID;
import com.dabkyu.dabkyu.entity.ReviewEntity;

public interface MemberReviewLikeRepository extends JpaRepository<MemberReviewLikeEntity, MemberReviewLikeEntityID>{

    public MemberReviewLikeEntity findByReviewSeqnoAndEmail(ReviewEntity review, MemberEntity member);


}
