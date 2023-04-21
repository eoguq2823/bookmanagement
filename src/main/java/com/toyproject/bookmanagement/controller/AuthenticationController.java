package com.toyproject.bookmanagement.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyproject.bookmanagement.aop.annotation.ValidAspect;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.service.AuthenticationService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth") //이컨트롤러에 들어오는 것들은 /auth가 붙는다
@RequiredArgsConstructor
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	
	@ValidAspect
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginReqDto loginReqDto, BindingResult bindingResult) {
//		System.out.println(loginReqDto);
		return ResponseEntity.ok().body(authenticationService.signin(loginReqDto)); // ok만 바디안에 값넣기 가능
	}
	
//	@CrossOrigin 제거
	@ValidAspect
	@PostMapping("/signup")		//@Valid 이게 실제로 검사함 / SignupReqDto signupReqDto 형태로 이메일 패스워드 네임이 들어올거다.
	//@Valid  BindingResult bindingResult 는 세트임 
	//signup이 호출이되면 매개변수 값들이 다들어옴 그러면 aop가 반응을함.
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) {
		authenticationService.checkDuplicatedEmail(signupReqDto.getEmail());
		authenticationService.signup(signupReqDto); //유저테이블에는 인설트됨 하지만 유저테이블에만 주면안되고 권한까지 줘야함.
		return ResponseEntity.ok().body(true);
	}
	@GetMapping("/authenticated")
	public ResponseEntity<?> authenticated(String accessToken) {
		System.out.println(accessToken);
		
		return ResponseEntity.ok().body(authenticationService.authenticated(accessToken));
	}
	 
}
