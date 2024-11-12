package com.dabkyu.dabkyu.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CouponCategoryEntityID implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long couponSeqno;
    private Long category3Seqno;
    
}