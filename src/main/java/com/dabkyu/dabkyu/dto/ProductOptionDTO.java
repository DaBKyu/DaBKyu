package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.ProductOptionEntity;

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
public class ProductOptionDTO {
    private Long optionSeqno;
	private ProductEntity productSeqno;
	private String optCategory;
	private String optName;
	private int optPrice;

    public ProductOptionDTO(ProductOptionEntity entity) {
        this.optionSeqno = entity.getOptionSeqno();
        this.productSeqno = entity.getProductSeqno();
        this.optCategory = entity.getOptCategory();
        this.optName = entity.getOptName();
        this.optPrice = entity.getOptPrice(); 
    }

    public ProductOptionEntity dtoToEntity(ProductOptionDTO dto) {
        ProductOptionEntity entity = ProductOptionEntity.builder()
                                                        .optionSeqno(dto.getOptionSeqno())
                                                        .productSeqno(dto.getProductSeqno())
                                                        .optCategory(dto.getOptCategory())
                                                        .optName(dto.getOptName())
                                                        .optPrice(dto.getOptPrice())
                                                        .build();
        return entity;
    }
}
