package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {

	private final Key key;
									//yml에서 가져옴
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	
	public JwtRespDto generateToken(Authentication authentication) {
		
		StringBuilder builder = new StringBuilder();
		authentication.getAuthorities().forEach(authority -> {
			builder.append(authority.getAuthority());
			builder.append(",");
		});
		//다돌고 마지막 심표 제거위해서
		builder.delete(builder.length() - 1, builder.length()); // builder -> ROLE_USER, 
			 
		String authorities = builder.toString();
		
		//토큰 시간
		Date tokenExpiresDate = new Date (new Date().getTime() + ((1000 * 60) * 60)); 
		
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName()) 		// 토큰의 제목
				.claim("auth", authorities)						//auth
				.setExpiration(tokenExpiresDate) 			// 토큰 만료 시간
				.signWith(key, SignatureAlgorithm.HS256) 	//토큰 암호화
				.compact();
		
		return JwtRespDto.builder().grantType("Bearer").accessToken(accessToken).build();
	}
}
