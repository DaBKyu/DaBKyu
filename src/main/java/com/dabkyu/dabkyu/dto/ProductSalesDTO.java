package com.dabkyu.dabkyu.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder 
public class ProductSalesDTO {
    private String productName;
    private BigDecimal salesAmount;

      public ProductSalesDTO(String productName, BigDecimal salesAmount) {
        this.productName = productName;
        this.salesAmount = salesAmount;
    }
}
