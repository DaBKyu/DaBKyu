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
public class SignupAgeStatDTO {
    private String ageGroup;
    private BigDecimal ageGroupCount;

}
