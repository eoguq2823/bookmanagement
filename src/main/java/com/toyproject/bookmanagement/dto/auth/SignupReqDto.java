package com.toyproject.bookmanagement.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class SignupReqDto {
	
	@Email  //validation
	private String email;
	
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
			message = "비밀번호는 영문자, 숫자, 특수문자를 포험하여 8 ~ 16자로 작성") //정규식 달기
	private String password;
	
	@Pattern(regexp = "^[가-힣]{2,7}$",
			message = "한글이름만 작성 가능합니다.")
	private String name;
}
