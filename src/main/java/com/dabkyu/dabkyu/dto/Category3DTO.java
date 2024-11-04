package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;

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

    public Category3DTO(Category3Entity entity) {
        this.category3Seqno = entity.getCategory3Seqno();
        this.category2Seqno = entity.getCategory2Seqno();
        this.category3Name = entity.getCategory3Name();
    }

    public Category3Entity dtoToEntity(Category3DTO dto) { 
        Category3Entity category3Entity = Category3Entity.builder()
                                                         .category3Seqno(dto.getCategory3Seqno())
                                                         .category2Seqno(dto.getCategory2Seqno())
	                                                     .category3Name(dto.getCategory3Name())
                                                         .build();
        return category3Entity;
    }
}