package com.dabkyu.dabkyu.util;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String baseKey;
	private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	//키 설정
	private Key createKey() {
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(baseKey);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	    return signingKey;
	}
	
	//토큰 생성
	public String generateToken(Map<String,Object> payloads, int days) { //time : 분단위
		
		//헤더 부분 설정
		Map<String, Object> headers = new HashMap<String, Object>();
	    headers.put("typ", "JWT");
	    headers.put("alg", "HS256");

	    JwtBuilder builder = Jwts.builder()
	    						.setHeader(headers)
	    						.setClaims(payloads)
	    						.setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
	    						.setExpiration(Date.from(ZonedDateTime.now().plusDays(days).toInstant()))
	    						.signWith(createKey(), signatureAlgorithm);

	    String result = builder.compact(); //
	    //log.info("JWT = {}",result);
	    return result;
	}
	
	//토큰 유효성 검사
	public String validateToken(String token) {
		
		try {
			Jwts.parserBuilder().setSigningKey(createKey()).build().parseClaimsJws(token);
			return "VALID_JWT";
		}catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			return "INVALID_JWT";
		}catch(ExpiredJwtException e) {
			return "EXPIRED_JWT";
		}catch(UnsupportedJwtException e) {
			return "UNSUPPORTED_JWT";
		}catch(IllegalArgumentException e) {
			return "EMPTY_JWT";
		}
		
	}	
	
	//http Authorization 헤더에서 토큰 가져 오기
	public String getTokenFromAuthorization(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if(!bearerToken.isEmpty() && bearerToken.startsWith("Bearer")) //Header에 Bearer가 존재하면
		{
			return bearerToken.substring(7); //앞의 0-6까지의 문자는 짜르고 다음부터의 문자들을 가져 온다.
		} else return "INVALID_HEADER"; //Header에 Bearer가 존재하지 않음
	}
	
	//토큰에서 email 추출
	public Map<String,Object> getDataFromToken(String token) throws Exception{
		
		Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(baseKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
		
		Map<String, Object> data = new HashMap<>();
		data.put("email", claims.get("email").toString());
		//data.put("password", claims.get("password").toString());
		
		return data;
	}	
	
}
