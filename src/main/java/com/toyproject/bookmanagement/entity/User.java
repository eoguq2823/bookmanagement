package com.toyproject.bookmanagement.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//최대한 카멜표기씀
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	private int userId;
	private String email;
	private String password;
	private String name;
	private String provider;
	
	//하나의 유저는 여러개의 권한을 가질 수 있다.
	private List<Authority> authorities;
	
}
