package com.toyproject.bookmanagement.exception;

import java.util.Map;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -794630080252143530L;
	
	private Map<String, String> errorMap;
	
	public CustomException(String message) { //RuntimeException에다가 메세지 보내는거
		super(message);
	}
	
	public CustomException(String message, Map<String, String> errorMap) { 
		super(message);
		this.errorMap = errorMap;
	}
}
