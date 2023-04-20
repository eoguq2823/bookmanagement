package com.toyproject.bookmanagement.repository;

import org.apache.ibatis.annotations.Mapper;

import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;

@Mapper
public interface UserRepository {

	//db에서 email찾기~
	public User findUserByEmail(String email);
	public int saveUser(User user);
	
	public int saveAuthority(Authority authority);
}