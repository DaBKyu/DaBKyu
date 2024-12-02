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
@AllArgsConstructor
@Builder
public class SalesByAgeGroupDTO {
    private String ageGroup; 
    private BigDecimal totalSales;

}
