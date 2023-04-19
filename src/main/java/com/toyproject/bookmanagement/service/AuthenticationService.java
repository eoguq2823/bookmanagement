package com.toyproject.bookmanagement.service;

import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.RegisteUserReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {	
	
	private final UserRepository userRepository;
	
	public void checkDuplicatedEmail(String email) { //이메일만 중복확인
		//db에있는지 확인위해 UserRepository로 이동
		if(userRepository.findUserByEmail(email) != null) { //errorMap을 따로 클래스를 빼서 사용하는 구조
			throw new CustomException("Duplicated Email", 
					ErrorMap.builder()
							.put("email", "사용중인 이메일입니다.")
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
	}
}
