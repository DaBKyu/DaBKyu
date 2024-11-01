package com.dabkyu.dabkyu.entity;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberNotificationEntityID implements Serializable{

    private static final long serialVersionUID = 1L;
    private String email;
    private Long notificationSeqno;

}