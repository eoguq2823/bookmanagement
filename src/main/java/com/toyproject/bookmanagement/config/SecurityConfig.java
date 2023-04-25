package com.toyproject.bookmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.toyproject.bookmanagement.security.JwtAuthenticationEntryPoint;
import com.toyproject.bookmanagement.security.JwtAuthenticationFilter;
import com.toyproject.bookmanagement.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override // HttpSecurity객체를 가져와 해당 객체의 설정을 구성하는 메소드
	protected void configure(HttpSecurity http) throws Exception { //컨 + 스 => http
		http.cors();
		// CSRF (Cross-Site Request Forgery) 보호를 비활성화합니다. 이 보호 기능이 적절하게 구성되지 않으면 유효한 요청이 처리되지 않을 수 있습니다.
		http.csrf().disable(); //csrf를 사용안하겟다. 안하면 요청이 안날라간다.
		// HTTP 기본 인증을 비활성화합니다. 기본 인증은 사용자 이름과 비밀번호를 평문으로 보내기 때문에 보안적으로 취약합니다.
		http.httpBasic().disable();
		// 폼 기반 인증을 비활성화합니다. 폼 기반 인증은 사용자가 자격 증명을 폼에 입력하도록 요구하는 방식으로, 상태를 유지하지 않는 애플리케이션에 적합하지 않습니다.
		http.formLogin().disable();
		// 세션 생성 정책을 STATELESS로 설정합니다. 이는 이전 요청과 독립적인 각 요청에 대해 세션이 생성되지 않도록 합니다.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 세션 사용하지 않겠다.
		// 요청에 대한 인가 규칙 구성을 시작합니다.
		http.authorizeRequests()
			// 일치시킬 URL 패턴을 지정합니다. 이 경우, /auth/로 시작하는 모든 URL이 일치됩니다.
			.antMatchers("/auth/**") //앞에 auth가 붙으면 허용한다.
			// 이전 줄에서 지정한 URL 패턴에 해당하는 모든 요청을 허용합니다.
			.permitAll()
			// 이전 줄에서 지정한 URL 패턴에 일치하지 않는 모든 요청에 대한 규칙을 지정합니다.
			.anyRequest()
			// 이전 규칙에 해당하는 요청은 인증이 필요하다는 것을 나타냅니다.
			.authenticated() //인증을 거쳐라
			// 이전 인가 규칙을 끝내고 새로운 인가 규칙을 시작합니다.
			.and()
			// 필터 체인에 새 필터를 추가합니다. 이 필터는 JwtAuthenticationFilter의 인스턴스이며, UsernamePasswordAuthenticationFilter 필터 이전에 실행됩니다.
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class) //이쪽필터에서 실제로 검증을 할거임
			// 예외 처리 규칙 구성을 시작합니다.
			.exceptionHandling()
			// 인증 진입점을 authenticationEntryPoint 객체로 설정합니다. 인증 진입
			.authenticationEntryPoint(jwtAuthenticationEntryPoint);  
		
	}

}
