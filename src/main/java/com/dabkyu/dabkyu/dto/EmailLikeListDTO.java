package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.EmailCategoryEntity;
import com.dabkyu.dabkyu.entity.EmailEntity;
import com.dabkyu.dabkyu.entity.EmailLikeListEntity;
import com.dabkyu.dabkyu.entity.ProductEntity;

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
public class EmailLikeListDTO {

    private EmailEntity emailSeqno;
    private ProductEntity productSeqno;

    public EmailLikeListDTO(EmailLikeListEntity entity){
        this.emailSeqno = entity.getEmailSeqno();
        this.productSeqno = entity.getProductSeqno();
    }

    public EmailLikeListEntity dtoToEntity(EmailLikeListDTO dto){
        EmailLikeListEntity entity = EmailLikeListEntity.builder()
                                                        .emailSeqno(dto.getEmailSeqno())
                                                        .productSeqno(dto.getProductSeqno())
                                                        .build();
        return entity;
    }


}
