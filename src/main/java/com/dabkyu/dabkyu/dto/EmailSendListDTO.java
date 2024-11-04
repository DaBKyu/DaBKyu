package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import com.dabkyu.dabkyu.entity.EmailSendListEntity;
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
public class EmailSendListDTO {
	private Long emailSendlistSeqno;
	private String targetCategory;
	private String emailTitle;
	private String emailContent;
	private LocalDateTime emailSendDate;

    public EmailSendListDTO(EmailSendListEntity entity) {
        this.emailSendlistSeqno = entity.getEmailSendlistSeqno();
        this.targetCategory = entity.getTargetCategory();
        this.emailTitle = entity.getEmailTitle();
        this.emailContent = entity.getEmailContent();
        this.emailSendDate = entity.getEmailSendDate();
    }

    public EmailSendListEntity dtoToEntity(EmailSendListDTO dto) {
        EmailSendListEntity emailSendListEntity = EmailSendListEntity.builder()
                                                                     .targetCategory(dto.getTargetCategory())
                                                                     .emailTitle(dto.getEmailTitle())
                                                                     .emailContent(dto.getEmailContent())
                                                                     .emailSendDate(dto.getEmailSendDate())
                                                                     .build();
        return emailSendListEntity;                                                                
    }
}
