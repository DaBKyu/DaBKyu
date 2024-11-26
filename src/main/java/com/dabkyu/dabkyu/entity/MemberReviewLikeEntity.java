package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="memberReviewlike")
@Table(name="member_review_like")
@IdClass(MemberReviewLikeEntityID.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReviewLikeEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email", nullable = false)
	private MemberEntity email;

    @Id
    @Column(name="review_seqno",nullable=false)
	private Long reviewSeqno;

    @Column(name="regdate",nullable = false)
    private LocalDateTime regdate;

}
