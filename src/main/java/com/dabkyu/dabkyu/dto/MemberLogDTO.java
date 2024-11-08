package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.MemberLogEntity;

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
public class MemberLogDTO {
	
	private MemberEntity email;
	private LocalDateTime inouttime;
	private String status;
	
	public MemberLogDTO(MemberLogEntity memberLogEntity) {
		this.email = memberLogEntity.getEmail();
		this.inouttime = memberLogEntity.getInouttime();
		this.status = memberLogEntity.getStatus();
	}
	
	public MemberLogEntity dtoToEntity(MemberLogDTO dto) {
		
		MemberLogEntity memberLogEntity = MemberLogEntity.builder()
														.email(dto.getEmail())
														.inouttime(dto.getInouttime())
														.status(dto.getStatus())
														.build();
		return memberLogEntity;
	}
}