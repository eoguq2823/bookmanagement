package com.toyproject.bookmanagement.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

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
}
