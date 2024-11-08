package com.dabkyu.dabkyu.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberLogEntityID implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String email;
	private LocalDateTime inouttime;
}