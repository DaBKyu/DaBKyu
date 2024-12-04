package com.dabkyu.dabkyu.dto;

import java.util.ArrayList;
import java.util.List;

import com.dabkyu.dabkyu.entity.Category3Entity;
import com.dabkyu.dabkyu.entity.ProductEntity;
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
public class ProductDTO {
    private Long productSeqno;
	private Category3Entity category3Seqno;
    private String productName;
	private String productInfo;
	private int price;
	private int stockAmount;
	private String deliveryisFree;
	private int likecnt;
	// private String infoOrgImage;
	private String thumbnail;
    private String secretYn;
    private List<ProductFileDTO> productFiles;

    public ProductDTO(ProductEntity entity) {
        this.productSeqno = entity.getProductSeqno();
        this.category3Seqno = entity.getCategory3Seqno();
        this.productName = entity.getProductName();
        this.productInfo = entity.getProductInfo();
        this.price = entity.getPrice();
        this.stockAmount = entity.getStockAmount();
        this.deliveryisFree = entity.getDeliveryisFree();
        this.likecnt = entity.getLikecnt();
        // this.infoOrgImage = entity.getInfoOrgImage();
        // this.infoStoredImage = entity.getInfoStoredImage(); 
        this.secretYn = entity.getSecretYn();
        this.productFiles = new ArrayList<>();
    }

    public ProductEntity dtoToEntity(ProductDTO dto) {
        ProductEntity entity = ProductEntity.builder()
                                            .productSeqno(dto.getProductSeqno())
                                            .category3Seqno(dto.getCategory3Seqno())
                                            .productName(dto.getProductName())
                                            .productInfo(dto.getProductInfo())
                                            .price(dto.getPrice())
                                            .stockAmount(dto.getStockAmount())
                                            .deliveryisFree(dto.getDeliveryisFree())
                                            .likecnt(dto.getLikecnt())
                                            // .infoOrgImage(dto.getInfoOrgImage())
                                            // .infoStoredImage(dto.getInfoStoredImage())
                                            .secretYn(dto.getSecretYn())
                                            .build();
        return entity;
    }
}
