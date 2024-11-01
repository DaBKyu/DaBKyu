package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category1Entity;

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
public class Category2DTO {

    private Long category2Seqno;
    private Category1Entity category1Seqno;
	private String category2Name;
}
