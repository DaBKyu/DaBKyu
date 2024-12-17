package com.dabkyu.dabkyu.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
    private String gender;
    private LocalDate birthDate;
    private String memberGrade;
    private String pay;
    private LocalDateTime regdate;
    private LocalDateTime lastloginDate;
    private LocalDateTime lastlogoutDate;
    private LocalDateTime lastpwDate;
    private LocalDateTime lastpwcheckDate;
    private String fromSocial;
    private int pwcheck;
    private int point;
    private String role;
    private String notificationYn;
    private String emailRecept;
    private LocalDateTime emailReceptDate;
    private String authkey;
    private int totalPvalue;
    private String birth;
    
    public MemberDTO(MemberEntity memberEntity) {
        this.email = memberEntity.getEmail();
        this.password = memberEntity.getPassword();
        this.telno = memberEntity.getTelno();
        this.username = memberEntity.getUsername();
        this.gender = memberEntity.getGender();
        this.birthDate = memberEntity.getBirthDate();
        this.memberGrade = memberEntity.getMemberGrade();
        this.pay = memberEntity.getPay();
        this.regdate = memberEntity.getRegdate();
        this.lastloginDate = memberEntity.getLastloginDate();
        this.lastlogoutDate = memberEntity.getLastlogoutDate();
        this.lastpwDate = memberEntity.getLastpwDate();
        this.lastpwcheckDate = memberEntity.getLastpwcheckDate();
        this.fromSocial = memberEntity.getFromSocial();
        this.point = memberEntity.getPoint();
        this.role = memberEntity.getRole();
        this.notificationYn = memberEntity.getNotificationYn();
        this.emailRecept = memberEntity.getEmailRecept();
        this.emailReceptDate = memberEntity.getEmailReceptDate();
        this.authkey = memberEntity.getAuthkey();
        this.totalPvalue = memberEntity.getTotalPvalue();
    }

    public MemberEntity dtoEntity(MemberDTO dto) {

        MemberEntity memberEntity = MemberEntity.builder()
                                                .email(dto.getEmail())
                                                .password(dto.getPassword())
                                                .telno(dto.getTelno())
                                                .username(dto.getUsername())
                                                .gender(dto.getGender())
                                                .birthDate(dto.getBirthDate())
                                                .memberGrade(dto.getMemberGrade())
                                                .pay(dto.getPay())
                                                .regdate(dto.getRegdate())
                                                .lastloginDate(dto.getLastloginDate())
                                                .lastlogoutDate(dto.getLastlogoutDate())
                                                .lastpwDate(dto.getLastpwDate())
                                                .lastpwcheckDate(dto.getLastpwcheckDate())
                                                .fromSocial(dto.getFromSocial())
                                                .point(dto.getPoint())
                                                .role(dto.getRole())
                                                .notificationYn(dto.getNotificationYn())
                                                .emailRecept(dto.getEmailRecept())
                                                .emailReceptDate(dto.getEmailReceptDate())
                                                .authkey(dto.getAuthkey())
                                                .totalPvalue(dto.getTotalPvalue())
                                                .build();
        return memberEntity;
    }
}
