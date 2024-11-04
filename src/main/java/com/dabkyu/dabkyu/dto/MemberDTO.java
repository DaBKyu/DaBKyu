package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import com.dabkyu.dabkyu.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    
    private String email;
    private String password;
    private String telno;
    private String username;
    private String memberGrade;
    private String pay;
    private LocalDateTime regdate;
    private LocalDateTime lastloginDate;
    private LocalDateTime lastlogoutDate;
    private LocalDateTime lastpwDate;
    private String fromSocial;
    private int pwcheck;
    private int point;
    private String myCategories;
    private String role;
    private String notificationYn;
    private String emailRecept;
    private LocalDateTime emailReceptDate;
    
    public MemberDTO(MemberEntity memberEntity) {
        this.email = memberEntity.getEmail();
        this.password = memberEntity.getEmail();
        this.telno = memberEntity.getTelno();
        this.username = memberEntity.getUsername();
        this.memberGrade = memberEntity.getMemberGrade();
        this.pay = memberEntity.getPay();
        this.regdate = memberEntity.getRegdate();
        this.lastloginDate = memberEntity.getLastloginDate();
        this.lastlogoutDate = memberEntity.getLastlogoutDate();
        this.lastpwDate = memberEntity.getLastpwDate();
        this.fromSocial = memberEntity.getFromSocial();
        this.pwcheck = memberEntity.getPoint();
        this.point = memberEntity.getPoint();
        this.myCategories = memberEntity.getMyCategories();
        this.role = memberEntity.getRole();
        this.notificationYn = memberEntity.getNotificationYn();
        this.emailRecept = memberEntity.getEmailRecept();
        this.emailReceptDate = memberEntity.getEmailReceptDate();
    }

    public MemberEntity dtoEntity(MemberDTO dto) {

        MemberEntity memberEntity = MemberEntity.builder()
                                                .email(dto.getEmail())
                                                .password(dto.getPassword())
                                                .telno(dto.getTelno())
                                                .username(dto.getUsername())
                                                .memberGrade(dto.getMemberGrade())
                                                .pay(dto.getPay())
                                                .regdate(dto.getRegdate())
                                                .lastloginDate(dto.getLastloginDate())
                                                .lastlogoutDate(dto.getLastlogoutDate())
                                                .lastpwDate(dto.getLastpwDate())
                                                .fromSocial(dto.getFromSocial())
                                                .pwcheck(dto.getPwcheck())
                                                .point(dto.getPoint())
                                                .myCategories(dto.getMyCategories())
                                                .role(dto.getRole())
                                                .notificationYn(dto.getNotificationYn())
                                                .emailRecept(dto.getEmailRecept())
                                                .emailReceptDate(dto.getEmailReceptDate())
                                                .build();
        return memberEntity;
    }
}
