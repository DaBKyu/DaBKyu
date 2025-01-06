package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category1Entity;
import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.repository.Category1Repository;

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
    private Long category1Seqno;
	private String category2Name;

	private Category1Repository category1Repository;

	public Category2DTO(Long category2Seqno, Long category1Seqno, String category2Name) {
        this.category2Seqno = category2Seqno;
        this.category1Seqno = category1Seqno;
        this.category2Name = category2Name;
    }

    public Category2DTO(Category2Entity entity) {
		this.category2Seqno = entity.getCategory2Seqno();
        this.category1Seqno = entity.getCategory1Seqno().getCategory1Seqno();
		this.category2Name = entity.getCategory2Name();
	}

	public Category2Entity dtoToEntity(Category2DTO dto) {
		Category2Entity entity = Category2Entity.builder()
												.category2Seqno(dto.getCategory2Seqno())
                                                .category1Seqno(category1Repository.findById(dto.getCategory1Seqno()).get())
												.category2Name(dto.getCategory2Name())
												.build();
		return entity;
	}
}
