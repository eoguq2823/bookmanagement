package com.toyproject.bookmanagement.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toyproject.bookmanagement.dto.common.ErrorResponseDto;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		//예외 응답 
		response.setContentType(MediaType.APPLICATION_JSON_VALUE); //sprong프래임워크.http꺼임
		response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401에러
		ErrorResponseDto<?> errorResponseDto = 
				new ErrorResponseDto<AuthenticationException>("인증실패", authException);
		ObjectMapper objectMapper = new ObjectMapper(); 
		//객체를 넣으며 알아서 Json으로 변환해줌
		String responseJson = objectMapper.writeValueAsString(errorResponseDto);
		
		PrintWriter out = response.getWriter();
		out.println(responseJson);
		
	}

}
