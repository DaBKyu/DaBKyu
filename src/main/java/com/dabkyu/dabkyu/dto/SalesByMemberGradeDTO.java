package com.dabkyu.dabkyu.dto;

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
public class SalesByMemberGradeDTO {
    private String memberGrade;
    private Long totalPvalue;

}
