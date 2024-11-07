package com.dabkyu.dabkyu.util;

import java.util.Random;

public class PasswordMaker {

	//숫자 + 영문대소문자 7자리 임시패스워드 생성
	public String tempPasswordMaker() {
		
		StringBuffer tempPW = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 7; i++) {
		    int rIndex = rnd.nextInt(3); //0 ~ 2 의 숫자 중에서 랜덤하게 발생
		    switch (rIndex) {
		    case 0:
		        // a-z : 아스키코드 97~122 사이에 있는 값(소문자)을 임의로 가져 와서 Strinbuffer인 tempPW에 더하기
		    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 97));
		        break;
		    case 1:
		        // A-Z : 아스키코드 65~90 사이에 있는 값(대문자)을 임의로 가져 와서 Strinbuffer인 tempPW에 더하기
		    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 65));
		        break;
		    case 2:
		        // 0-9 : 숫자 0- 9 사이에 있는 값을 임의로 가져 와서 Strinbuffer인 tempPW에 더하기
		    	tempPW.append((rnd.nextInt(10)));
		        break;
		    }
		}		
		return tempPW.toString();	
	}
	
}
