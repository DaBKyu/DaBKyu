package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberNotificationEntity;
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
public class MemberNotificationDTO {
    private MemberEntity email;
	private NotificationEntity notificationSeqno;

    public MemberNotificationDTO(MemberNotificationEntity entity) {
        this.email = entity.getEmail();
        this.notificationSeqno = entity.getNotificationSeqno();
    }

    public MemberNotificationEntity dtoToEntity(MemberNotificationDTO dto) {
        MemberNotificationEntity entity = MemberNotificationEntity.builder()
                                                                  .email(dto.getEmail())
                                                                  .notificationSeqno(dto.getNotificationSeqno())
                                                                  .build();
        return entity;
    }
}
