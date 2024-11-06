package com.dabkyu.dabkyu.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
@Entity(name="member")
@Table(name="member")
public class MemberEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ")	
	@SequenceGenerator(name="MEMBER_SEQ", sequenceName = "member_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="email",length=50,nullable=false)
	private String email;

    @Column(name="password",length=200,nullable=false)
	private String password;

    @Column(name="telno",length=20,nullable=true)
	private String telno;
	
	@Column(name="username",length=20,nullable=false)
	private String username;

	@Column(name="gender",length=10,nullable=false)
	private String gender;

	@Column(name="birth_date",nullable=false)
	private LocalDateTime birthDate;
	
	@Column(name="member_grade",length=20,nullable=false)
	private String memberGrade;
	
	@Column(name="pay",length=200,nullable=false)
	private String pay;

    @Column(name="regdate",nullable=false)
	private LocalDateTime regdate;

    @Column(name="lastlogin_date", nullable=true)
	private LocalDateTime lastloginDate;
	
	@Column(name="lastlogout_date", nullable=true)
	private LocalDateTime lastlogoutDate;
	
	@Column(name="lastpw_date", nullable=true)
	private LocalDateTime lastpwDate;

    @Column(name="fromSocial",length=2, nullable=false)
	private String fromSocial;

    @Column(name="pwcheck", nullable=false)
	private int pwcheck;

    @Column(name="point", nullable=true)
	private int point;

    @Column(name="my_categories",length=200,nullable=true)
	private String myCategories;

	@Column(name="role",length=20,nullable=true)
	private String role;
	
	@Column(name="notification_yn",length=2,nullable=false)
	private String notificationYn;
	
	@Column(name="email_recept",length=2,nullable=false)
	private String emailRecept;
	
    @Column(name="email_recept_date",nullable=true)
	private LocalDateTime emailReceptDate;

	@Column(name="authkey",length = 200,nullable=true)
	private String authkey;
}
