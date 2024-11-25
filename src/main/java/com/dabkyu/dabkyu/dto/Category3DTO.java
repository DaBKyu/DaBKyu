package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category2Entity;
import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.repository.Category2Repository;

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
    private Long category2Seqno;
	private String category3Name;
    private String isTemporary;
    

    private Category2Repository category2Repository;

    public Category3DTO(Long category3Seqno, Long category2Seqno, String category3Name) {
        this.category3Seqno = category3Seqno;
        this.category2Seqno = category2Seqno;
        this.category3Name = category3Name;
    }

    public Category3DTO(Category3Entity entity) {
        this.category3Seqno = entity.getCategory3Seqno();
        this.category2Seqno = entity.getCategory2Seqno().getCategory2Seqno();
        this.category3Name = entity.getCategory3Name();
        this.isTemporary = entity.getIsTemporary();
    }

    public Category3Entity dtoToEntity(Category3DTO dto) { 
        Category3Entity category3Entity = Category3Entity.builder()
                                                         .category3Seqno(dto.getCategory3Seqno())
                                                         .category2Seqno(category2Repository.findById(dto.getCategory2Seqno()).get())
	                                                     .category3Name(dto.getCategory3Name())
                                                         .isTemporary(dto.getIsTemporary())
                                                         .build();
        return category3Entity;
    }
}