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
public class SignupGenderStatDTO {
    private String gender;
    private Long genderCount;

    

}
