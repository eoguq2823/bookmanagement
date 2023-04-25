package com.toyproject.bookmanagement.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final JwtTokenProvider jwtTokenProvider;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String accessToken = httpRequest.getHeader("Authorization");
		accessToken = jwtTokenProvider.getToken(accessToken); // Bearer 뺴주는 것
		boolean validationFlag = jwtTokenProvider.validateToken(accessToken); // 토큰의유효성검사
		
		if(validationFlag) {
			Authentication authentication = jwtTokenProvider.getAuthentication(accessToken); 
			SecurityContextHolder.getContext().setAuthentication(authentication); //setAuthentication() 여기안에 등록이 되어야지만 로그인이 된것.
		}
		
		chain.doFilter(request, response);
		
	}

}
