package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
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


}
