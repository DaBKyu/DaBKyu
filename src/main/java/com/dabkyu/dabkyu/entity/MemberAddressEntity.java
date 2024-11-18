package com.dabkyu.dabkyu.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name="memberAddress")
@Table(name="member_address")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddressEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBERADDRESS_SEQ")	
	@SequenceGenerator(name="MEMBERADDRESS_SEQ", sequenceName = "memberaddress_seq", 
		initialValue = 1, allocationSize = 1)
	@Column(name="member_address_seqno")
	private Long memberAddressSeqno;
	
    @ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="email", nullable = false)
	private MemberEntity email;

	@Column(name="addrName", length=20,nullable=false)
	private String addrName;

    @Column(name="address", length=200, nullable=false)
	private String address;

    @Column(name="zipcode", length=20, nullable=false)
	private String zipcode;

    @Column(name="detail_addr", length=20, nullable=false)
	private String detailAddr;

	@Column(name="request",length=200,nullable=true)
	private String request;

	@Column(name="receiver_name",length=20,nullable=true)
	private String receiverName;

	@Column(name="receiver_telno",length=20,nullable=true)
	private String receiverTelno;

	@Column(name="isBasic",length=2,nullable=false)
	private String isBasic;
	
}
