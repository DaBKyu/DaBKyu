package com.dabkyu.dabkyu.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.dabkyu.dabkyu.dto.MemberDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	@Column(name="email",length=50,nullable=false)
	private String email;

    @Column(name="password",length=200,nullable=false)
	private String password;

    @Column(name="telno",length=20,nullable=true)
	private String telno;
	
	@Column(name="username",length=20,nullable=false)
	private String username;

	@Column(name="gender",length=10,nullable=true)
	private String gender;

	@Column(name="birth_date",nullable=true)
	private LocalDate birthDate;
	
	@Column(name="member_grade",length=20,nullable=false)
	private String memberGrade;
	
	@Column(name="pay",length=200,nullable=true)
	private String pay;

    @Column(name="regdate",nullable=false)
	private LocalDateTime regdate;

    @Column(name="lastlogin_date",nullable=true)
	private LocalDateTime lastloginDate;
	
	@Column(name="lastlogout_date",nullable=true)
	private LocalDateTime lastlogoutDate;
	
	@Column(name="lastpw_date",nullable=true)
	private LocalDateTime lastpwDate;

    @Column(name="fromSocial",length=2,nullable=false)
	private String fromSocial;

    @Column(name="pwcheck",nullable=false)
	private int pwcheck;

    @Column(name="point")
	private int point;

    // @Column(name="my_categories",length=200,nullable=true)
	// private String myCategories;

	@Column(name="role",length=20,nullable=true)
	private String role;
	
	@Column(name="notification_yn",length=2,nullable=false)
	private String notificationYn;
	
	@Column(name="email_recept",length=2,nullable=false)
	private String emailRecept;
	
    @Column(name="email_recept_date",nullable=true)
	private LocalDateTime emailReceptDate;
    
	@Column(name="authkey",length=200,nullable=true)
	private String authkey;

	@Column(name="total_pvalue")
	private int totalPvalue;

	@Column(name="is_active", length=2, nullable=false)
    private String isActive;
	
	public void modifyMemberInfo(MemberDTO dto) {
        this.telno = dto.getTelno();
        this.pay = dto.getPay();
        this.notificationYn = dto.getNotificationYn();
        this.emailRecept = dto.getEmailRecept();
	}
}
