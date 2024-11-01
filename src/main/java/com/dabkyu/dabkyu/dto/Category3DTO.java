package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category2Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category3DTO {

    private Long category3Seqno;
    private Category2Entity category2Seqno;
	private String category3Name;
}