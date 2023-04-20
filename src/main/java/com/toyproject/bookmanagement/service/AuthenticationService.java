package com.toyproject.bookmanagement.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService implements UserDetailsService {	
	
	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	public void checkDuplicatedEmail(String email) { //이메일만 중복확인
		//db에있는지 확인위해 UserRepository로 이동
		if(userRepository.findUserByEmail(email) != null) { //errorMap을 따로 클래스를 빼서 사용하는 구조
			throw new CustomException("Duplicated Email", 
					ErrorMap.builder().put("email", "사용중인 이메일입니다.")
//							.put("여기다", "또 put할수있음")
							.build());
		}
	}
	
	public void signup(SignupReqDto signupReqDto) {
		User userEntity = signupReqDto.toEntity();
//		userEntity.setEmail(signupReqDto.getEmail());
//		userEntity.setPassword(signupReqDto.getPassword());
//		userEntity.setName(signupReqDto.getName()); //이렇게 해주면 코드가 지저분해지니깐 사인업Dto에 만들어줌
		userRepository.saveUser(userEntity);
		userRepository.saveAuthority(Authority.builder()
				.userId(userEntity.getUserId())
				.roleId(1)
				.build());
	}
	
	// 이메일과 패스워드를 매니저가 알아볼수 있게.
	public JwtRespDto signin(LoginReqDto loginReqDto) {
		
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword());
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		System.out.println(authentication.getPrincipal());
		
		return null;
		//이거 땅하는 순간 요청때 들어온 아이디 패스워드와 DB의 아이디패스워드를 비교함 
		
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User userEntity = userRepository.findUserByEmail(username);
		
		if(userEntity == null) {
			throw new CustomException("로그인 실패", ErrorMap.builder().put("email", "사용자 정보를 확인하세요.").build());
		}
		
		return null;
	}
	
}
