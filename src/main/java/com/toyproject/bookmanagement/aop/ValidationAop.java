package com.toyproject.bookmanagement.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.toyproject.bookmanagement.exception.CustomException;

@Aspect
@Component
public class ValidationAop {

	@Pointcut("@annotation(com.toyproject.bookmanagement.aop.annotation.ValidAspect)")
	private void pointCut() {}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

//		ValidAspect에 에러가 있는지 확인
		Object[] args = joinPoint.getArgs();
		//1번째있을수도 2번쨰있을수도 ~~있으니깐 반복돌려서 찾아야함
		BindingResult bindingResult = null;
		
		for(Object arg : args) {
			if(arg.getClass() == BeanPropertyBindingResult.class) {
				bindingResult = (BeanPropertyBindingResult) arg;
			}
		}
		
		if(bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			bindingResult.getFieldErrors().forEach(error -> {
				errorMap.put(error.getField(), error.getDefaultMessage());
			});
			
			throw new CustomException("Validation Failed", errorMap); //예외를 던지니깐 받을 컨트롤러 필요
		}
		
		return joinPoint.proceed();
	}
}
