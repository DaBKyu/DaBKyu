package com.dabkyu.dabkyu;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

}
