package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.EmailEntity;
import com.dabkyu.dabkyu.entity.EmailFileEntity;
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
public class EmailFileDTO {
    private Long emailfileSeqno;
    private EmailEntity emailSeqno;
    private String orgFilename;
    private String storedFilename;

    public EmailFileDTO(EmailFileEntity entity) {
        this.emailfileSeqno = entity.getEmailfileSeqno();
        this.emailSeqno = entity.getEmailSeqno();
        this.orgFilename = entity.getOrgFilename();
        this.storedFilename = entity.getStoredFilename();
    }

    public EmailFileEntity dtoToEntity(EmailFileDTO dto) {
        EmailFileEntity emailFileEntity = EmailFileEntity.builder()
                                                         .emailfileSeqno(dto.getEmailfileSeqno())
                                                         .emailSeqno(dto.getEmailSeqno())
                                                         .orgFilename(dto.getOrgFilename())
                                                         .storedFilename(dto.getStoredFilename())
                                                         .build();
        return emailFileEntity;
    }


    
    
}
