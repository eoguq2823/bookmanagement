package com.toyproject.bookmanagement.service;

import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {	
	
	private final UserRepository userRepository;
	
	public void duplicatedEmail(String email) { //이메일만 중복확인
		//db에있는지 확인위해 UserRepository로 이동
	}
}
