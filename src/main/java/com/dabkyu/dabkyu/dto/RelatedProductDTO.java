package com.dabkyu.dabkyu.dto;

import com.dabkyu.dabkyu.entity.ProductEntity;
import com.dabkyu.dabkyu.entity.RelatedProductEntity;
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
public class RelatedProductDTO {
    private Long relatedproductSeqno;
	private ProductEntity productSeqno;
	private String relatedproductCategory;
	private String relatedproductName;
	private int relatedproductPrice;
	
	public RelatedProductDTO(RelatedProductEntity entity) {
		this.relatedproductSeqno = entity.getRelatedproductSeqno();
		this.productSeqno = entity.getProductSeqno();
		this.relatedproductCategory = entity.getRelatedproductCategory();
		this.relatedproductName = entity.getRelatedproductName();
		this.relatedproductPrice = entity.getRelatedproductPrice();
	}

	public RelatedProductEntity dtoToEntity(RelatedProductDTO dto) {
		RelatedProductEntity entity = RelatedProductEntity.builder()
														  .relatedproductSeqno(dto.getRelatedproductSeqno())
														  .productSeqno(dto.getProductSeqno())
														  .relatedproductCategory(dto.getRelatedproductCategory())
														  .relatedproductName(dto.getRelatedproductName())
														  .relatedproductPrice(dto.getRelatedproductPrice())
														  .build();
		return entity;												  
	}
}
