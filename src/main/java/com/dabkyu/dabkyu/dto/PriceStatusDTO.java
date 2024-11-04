package com.dabkyu.dabkyu.dto;

import java.time.LocalDateTime;
import com.dabkyu.dabkyu.entity.PriceStatusEntity;
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
public class PriceStatusDTO {
    private ProductEntity productSeqno;
	private int price;
	private LocalDateTime priceChangeDate;

    public PriceStatusDTO(PriceStatusEntity entity) {
        this.productSeqno = entity.getProductSeqno();
        this.price = entity.getPrice();
        this.priceChangeDate = entity.getPriceChangeDate();
    }

    public PriceStatusEntity dtoToEntity(PriceStatusDTO dto) {
        PriceStatusEntity entity = PriceStatusEntity.builder()
                                                    .productSeqno(dto.getProductSeqno())
                                                    .price(dto.getPrice())
                                                    .priceChangeDate(dto.getPriceChangeDate())
                                                    .build();
        return entity;
    }
}
