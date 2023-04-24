package com.toyproject.bookmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception { //컨 + 스 => http
		http.csrf().disable(); //csrf를 사용안하겟다. 안하면 요청이 안날라간다.
		http.httpBasic().disable();
		http.formLogin().disable();
		
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 사용하지 않겠다.
		
		http.authorizeRequests()
			.antMatchers("/auth/**") //앞에 auth가 붙으면 허용한다.
			.permitAll();
//			.anyRequest()
//			.authenticated(); //인증을 거쳐라
		
	}

}
