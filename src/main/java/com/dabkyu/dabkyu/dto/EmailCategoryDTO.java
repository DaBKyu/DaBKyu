package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.EmailCategoryEntity;
import com.dabkyu.dabkyu.entity.EmailEntity;

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
public class EmailCategoryDTO {

    private EmailEntity emailSeqno;
    private Category3Entity category3Seqno;

    public EmailCategoryDTO(EmailCategoryEntity entity){
        this.emailSeqno = entity.getEmailSeqno();
        this.category3Seqno = entity.getCategory3Seqno();
    }

    public EmailCategoryEntity dtoToEntity(EmailCategoryDTO dto){
        EmailCategoryEntity entity = EmailCategoryEntity.builder()
                                                        .emailSeqno(dto.getEmailSeqno())
                                                        .category3Seqno(dto.getCategory3Seqno())
                                                        .build();
        return entity;
    }
}
