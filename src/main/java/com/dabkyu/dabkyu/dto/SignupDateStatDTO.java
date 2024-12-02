package com.dabkyu.dabkyu.dto;

import java.time.LocalDate;

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
public class SignupDateStatDTO {
    private LocalDate regDate;    
    private Long signupCount; 

}
