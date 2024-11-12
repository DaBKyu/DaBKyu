package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductInfoFileEntity;

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
public class ProductInfoFileDTO {
    private Long productInfoFileSeqno;
	private ProductEntity productSeqno;
	private String orgFilename;
	private String storedFilename;

    public ProductInfoFileDTO(ProductInfoFileEntity entity) {
        this.productInfoFileSeqno = entity.getProductInfoFileSeqno();
        this.productSeqno = entity.getProductSeqno();
        this.orgFilename = entity.getOrgFilename();
        this.storedFilename = entity.getStoredFilename();
    }

    public ProductInfoFileEntity dtoToEntity(ProductInfoFileDTO dto) {
        ProductInfoFileEntity entity = ProductInfoFileEntity.builder()
                                                                                        .productInfoFileSeqno(dto.getProductInfoFileSeqno())
                                                                                        .productSeqno(dto.getProductSeqno())
                                                                                        .orgFilename(dto.getOrgFilename())
                                                                                        .storedFilename(dto.getOrgFilename())
                                                                                        .build();
        return entity;                                 
    }
}
