package com.dabkyu.dabkyu.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {

    private String zipcode;
	private String province;
	private String road;
	private String building;
	private String oldaddr;

}
