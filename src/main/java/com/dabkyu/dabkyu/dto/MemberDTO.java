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
@NoArgsConstructor
@AllArgsConstructor
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
	private String fromsocial;
	private int pwcheck;
	private int point;
	private String myCategories;
	private String role;
	private String notificationYn;
	private String emailRecept;
	private LocalDateTime emailReceptDate;

	public MemberDTO(MemberEntity entity) {
	this.email = entity.getEmail();
	this.password = entity.getPassword();
	this.telno = entity.getTelno();
	this.username = entity.getUsername();
	this.memberGrade = entity.getMemberGrade();
	this.pay = entity.getPay();
	this.regdate = entity.getRegdate();
	this.lastloginDate = entity.getLastloginDate();
	this.lastlogoutDate = entity.getLastlogoutDate();
	this.lastpwDate = entity.getLastpwDate();
	this.fromsocial = entity.getFromsocial();
	this.pwcheck = entity.getPwcheck();
	this.point = entity.getPoint();
	this.myCategories = entity.getMyCategories();
	this.role = entity.getRole();
	this.notificationYn = entity.getNotificationYn();
	this.emailRecept = entity.getEmailRecept();
	this.emailReceptDate = entity.getEmailReceptDate();
	}

	public MemberEntity dtoToEntity(MemberDTO dto) {
		MemberEntity entity = MemberEntity.builder()
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
										  .fromsocial(dto.getFromsocial())
										  .pwcheck(dto.getPwcheck())
										  .point(dto.getPoint())
										  .myCategories(dto.getMyCategories())
										  .role(dto.getRole())
										  .notificationYn(dto.getNotificationYn())
										  .emailRecept(dto.getEmailRecept())
										  .emailReceptDate(dto.getEmailReceptDate())
										  .build();

		return entity;
	}
}
