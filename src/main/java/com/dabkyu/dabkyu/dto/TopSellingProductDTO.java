package com.dabkyu.dabkyu.dto;

import java.math.BigDecimal;

import com.dabkyu.dabkyu.entity.ProductEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TopSellingProductDTO {
    private String productName;
    private long totalAmount;

    public TopSellingProductDTO(String productName, long totalAmount) {
        this.productName = productName;
        this.totalAmount = totalAmount;
    }


}
