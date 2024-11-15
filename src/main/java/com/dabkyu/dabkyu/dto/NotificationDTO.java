package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import com.dabkyu.dabkyu.entity.NotificationEntity;
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
public class NotificationDTO {
    private Long notificationSeqno;
	private String notificationName;
	private LocalDateTime notificationDate;

    public NotificationDTO(NotificationEntity entity) {
        this.notificationSeqno = entity.getNotificationSeqno();
        this.notificationName = entity.getNotificationName();
        this.notificationDate = entity.getNotificationDate();
    }

    public NotificationEntity dtoToEntity(NotificationDTO dto) {
        NotificationEntity entity = NotificationEntity.builder()
                                                      .notificationSeqno(dto.getNotificationSeqno())
                                                      .notificationName(dto.getNotificationName())
                                                      .notificationDate(dto.getNotificationDate())
                                                      .build();
        return entity;
    }
}
