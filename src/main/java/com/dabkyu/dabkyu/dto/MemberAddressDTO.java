package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.MemberAddressEntity;
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
public class MemberAddressDTO {
	private Long memberAddressSeqno;
	private MemberEntity email;
	private String addrName;
	private String address;
	private String zipcode;
	private String detailAddr;
	private String receiverName;
	private String receiverTelno;
	private String request;
	private String isBasic;

	public MemberAddressDTO(MemberAddressEntity entity) {
		this.memberAddressSeqno = entity.getMemberAddressSeqno();
		this.addrName = entity.getAddrName();
		this.email = entity.getEmail();
		this.address = entity.getAddress();
		this.zipcode = entity.getZipcode();
		this.detailAddr = entity.getDetailAddr();
		this.receiverName = entity.getReceiverName();
		this.receiverTelno = entity.getReceiverTelno();
		this.request = entity.getRequest();
		this.isBasic = entity.getIsBasic();
	}

	public MemberAddressEntity dtoToEntity(MemberAddressDTO dto) {
		MemberAddressEntity entity = MemberAddressEntity.builder()
														.memberAddressSeqno(dto.getMemberAddressSeqno())
														.email(dto.getEmail())
														.addrName(dto.getAddrName())
														.address(dto.getAddress())
														.zipcode(dto.getZipcode())
														.detailAddr(dto.getDetailAddr())
														.receiverName(dto.getReceiverName())
														.receiverTelno(dto.getReceiverTelno())
														.request(dto.getRequest())
														.isBasic(dto.getIsBasic())
														.build();
		return entity;
	}
}
