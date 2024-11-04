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
	private String address;
	private String zipcode;
	private String detailAddr;

	public MemberAddressDTO(MemberAddressEntity entity) {
		this.memberAddressSeqno = entity.getMemberAddressSeqno();
		this.email = entity.getEmail();
		this.address = entity.getAddress();
		this.zipcode = entity.getZipcode();
		this.detailAddr = entity.getDetailAddr();
	}

	public MemberAddressEntity dtoToEntity(MemberAddressDTO dto) {
		MemberAddressEntity entity = MemberAddressEntity.builder()
														.memberAddressSeqno(dto.getMemberAddressSeqno())
														.email(dto.getEmail())
														.address(dto.getAddress())
														.zipcode(dto.getZipcode())
														.detailAddr(dto.getDetailAddr())
														.build();
		return entity;
	}
}
