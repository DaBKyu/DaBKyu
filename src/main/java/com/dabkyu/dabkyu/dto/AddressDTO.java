package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.AddressEntity;

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
public class AddressDTO {
	private Long addressSeqno;
    private String zipcode;
	private String province;
	private String road;
	private String building;
	private String oldaddr;

	public AddressDTO(AddressEntity entity) {
        this.addressSeqno = entity.getAddressSeqno();
		this.zipcode = entity.getZipcode();
		this.province = entity.getProvince();
		this.road = entity.getRoad();
		this.building = entity.getBuilding();
		this.oldaddr = entity.getOldaddr();
	} 

	public AddressEntity dtoToEntity(AddressDTO dto){
		AddressEntity addressEntity = AddressEntity.builder()
                                                   .addressSeqno(dto.getAddressSeqno())
                                                   .zipcode(dto.getZipcode())
                                                   .province(dto.getProvince())
                                                   .road(dto.getRoad())
                                                   .building(dto.getBuilding())
                                                   .oldaddr(dto.getOldaddr())
                                                   .build();
        return addressEntity;
	}
}
