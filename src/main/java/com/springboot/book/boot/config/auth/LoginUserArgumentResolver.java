package com.springboot.book.boot.config.auth;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.springboot.book.boot.config.auth.dto.SessionUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
	// HandlerMethodArgumentResolver를 상속하여 custom argument resolver 생성.
	// argument resolver란 스프링에서 조건에 맞는 파라미터를 객체에 바인딩하여 컨트롤러로 넘겨주는데 사용되는 구현체
	private final HttpSession httpSession;

	@Override
	public boolean supportsParameter(MethodParameter parameter) { // 어느 파라미터에 적용할지
		boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null; // 해당 파라미터에 @LoginUser가 추가되어있고
		boolean isUserClass = SessionUser.class.equals(parameter.getParameterType()); // 해당 파라미터의 타입이 User라면 이 ArgumentResolver를 적용시켜라.
		return isLoginUserAnnotation && isUserClass;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		return httpSession.getAttribute("user"); // 세션에서 user 반환
		// 다른 사용자간에 동일한 세션키(user)를 사용하는데 어떻게 구분?
		// 클라이언트는 헤더에 발급받은 jsessionId를 포함한 쿠키를 전송. 이를 바탕으로 서버는 세션에서 해당 유저를 식별함.
	}
}
