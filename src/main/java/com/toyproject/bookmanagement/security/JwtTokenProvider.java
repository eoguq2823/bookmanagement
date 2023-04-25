package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.catalina.startup.UserDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;
import com.toyproject.bookmanagement.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
			builder.append(authority.getAuthority() + ",");
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
	
	public boolean validateToken(String token) {
	
		try {
			Jwts.parserBuilder()
				.setSigningKey(key) //키값으로 암호화된 토큰값을 풀어준다.
				.build()
				.parseClaimsJws(token); //Jws

			return true;
			
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired Jwt Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported Jwt Token", e);
		} catch (IllegalArgumentException e) {
			log.info("IllegalArgument JWT Token", e);
		} catch (Exception e) {
			log.info("JWT Token Error", e);
		}
		
		return false;
	}
	//토큰에 Bearer때주는 메소드
	public String getToken(String token) {
		String type = "Bearer";
		if(StringUtils.hasText(token) && token.startsWith(type)) {
			return token.substring(type.length() + 1);
		}
		
		return null;
	}
	
	// getbody안에 들어 있는 정보를 꺼내오는것. (권한, 유저이메일) 이 정보 가지고 DB에서 정보를 가져와야함. 
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public Authentication getAuthentication(String accessToken) {
		Authentication authentication = null;
		
		Claims claims = getClaims(accessToken);
		//문자열로 바꾸기전에 
		if(claims.get("auth") == null) {
			throw new CustomException("AccessToken에 권한 정보가 없습니다.");
		}
		
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		
		String auth = claims.get("auth").toString(); // get을 했을때 안이 비어있을수도있다. (권한이 없으면 Null이라고 문자열로 나옴) 
		for(String role : auth.split(",")) { //split하면 배열로 만들어줌
			authorities.add(new SimpleGrantedAuthority(role)); //쉼표로 자른녀석들을 role에 넣어줌
		}		
		
		UserDetails userDetails = new User(claims.getSubject(), "", authorities); //시큐리티에 있는 User (username, "", authorities)
		
		authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities); //이정보를가지고 authentication객체생성
		return authentication; 
	}
}
