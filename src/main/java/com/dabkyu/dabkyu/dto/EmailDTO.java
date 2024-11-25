package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
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
public class EmailDTO {
	private Long emailSeqno;
	private String emailTitle;
	private String emailContent;
	private LocalDateTime emailSendDate;

    public EmailDTO(EmailEntity entity) {
        this.emailSeqno = entity.getEmailSeqno();
        this.emailTitle = entity.getEmailTitle();
        this.emailContent = entity.getEmailContent();
        this.emailSendDate = entity.getEmailSendDate();
    }

    public EmailEntity dtoToEntity(EmailDTO dto) {
        EmailEntity emailEntity = EmailEntity.builder()
                                             .emailSeqno(dto.getEmailSeqno())
                                             .emailTitle(dto.getEmailTitle())
                                             .emailContent(dto.getEmailContent())
                                             .emailSendDate(dto.getEmailSendDate())
                                             .build();
        return emailEntity;                                                                
    }
}
