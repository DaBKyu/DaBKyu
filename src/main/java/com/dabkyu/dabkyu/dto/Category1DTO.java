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
public class Category1DTO {
	private Long category1Seqno;
	private String category1Name;
	private int category1Order;

	public Category1DTO(Category1Entity entity) {
		this.category1Seqno = entity.getCategory1Seqno();
		this.category1Name = entity.getCategory1Name();
		this.category1Order = entity.getCategory1Order();
	}

	public Category1Entity dtoToEntity(Category1DTO dto) {
		Category1Entity entity = Category1Entity.builder()
												.category1Seqno(dto.getCategory1Seqno())
												.category1Name(dto.getCategory1Name())
												.category1Order(dto.getCategory1Order())
												.build();
		return entity;
	}
}


