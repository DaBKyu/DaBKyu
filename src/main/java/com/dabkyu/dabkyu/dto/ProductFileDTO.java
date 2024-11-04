package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductFileEntity;

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
public class ProductFileDTO {
    private Long productfileSeqno;
	private ProductEntity productSeqno;
	private String orgFilename;
	private String storedFilename;

    public ProductFileDTO(ProductFileEntity entity) {
        this.productfileSeqno = entity.getProductfileSeqno();
        this.productSeqno = entity.getProductSeqno();
        this.orgFilename = entity.getOrgFilename();
        this.storedFilename = entity.getStoredFilename();
    }

    public ProductFileEntity dtoToEntity(ProductFileDTO dto) {
        ProductFileEntity entity = ProductFileEntity.builder()
                                                    .productfileSeqno(dto.getProductfileSeqno())
                                                    .productSeqno(dto.getProductSeqno())
                                                    .orgFilename(dto.getOrgFilename())
                                                    .storedFilename(dto.getOrgFilename())
                                                    .build();
        return entity;                                 
    }
}
