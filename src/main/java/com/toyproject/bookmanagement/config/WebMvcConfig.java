package com.toyproject.bookmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") //모든요청 (/auth, /book등)을 모드다 셋팅해준다.
				.allowedOrigins("*")
				.allowedMethods("*"); //모든 메소드에 열어준다
//				.allowedOrigins("http://localhost:3000"); //이 3000포트에서 요청오는 위에애들 다열어준다. (다른포트 5000, 8000에서 요청보내면 막힌다.)
	}			//test할때만 "*"
}
