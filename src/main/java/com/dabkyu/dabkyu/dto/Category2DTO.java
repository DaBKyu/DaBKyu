package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category1Entity;
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
public class Category2DTO {
    private Long category2Seqno;
    private Category1Entity category1Seqno;
	private String category2Name;

    public Category2DTO(Category2Entity entity) {
		this.category2Seqno = entity.getCategory2Seqno();
        this.category1Seqno = entity.getCategory1Seqno();
		this.category2Name = entity.getCategory2Name();
	}

	public Category2Entity dtoToEntity(Category2DTO dto) {
		Category2Entity entity = Category2Entity.builder()
												.category2Seqno(dto.getCategory2Seqno())
                                                .category1Seqno(dto.getCategory1Seqno())
												.category2Name(dto.getCategory2Name())
												.build();
		return entity;
	}
}
