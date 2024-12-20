package com.dabkyu.dabkyu.util;

import java.util.Random;

public class CouponCodeMaker {

	//숫자 + 영문대소문자 7자리 임시패스워드 생성
	public String tempCouponCodeMaker() {
		
		StringBuffer tempCode = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < 20; i++) {
		    int rIndex = rnd.nextInt(3); //0 ~ 2 의 숫자 중에서 랜덤하게 발생
		    switch (rIndex) {
		    case 0:
		        // A-Z : 아스키코드 65~90 사이에 있는 값(대문자)을 임의로 가져 와서 Strinbuffer인 tempPW에 더하기
		    	tempCode.append((char) ((int) (rnd.nextInt(26)) + 65));
		        break;
		    case 1:
		        // 0-9 : 숫자 0- 9 사이에 있는 값을 임의로 가져 와서 Strinbuffer인 tempPW에 더하기
		    	tempCode.append((rnd.nextInt(10)));
		        break;
		    }
		}		
		return tempCode.toString();	
	}
}

