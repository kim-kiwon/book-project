package com.springboot.book.boot.config.auth;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.springboot.book.boot.domain.user.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter { // 스프링 시큐리티 관련 설정파일
	private final CustomOauth2UserService customOauth2UserService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // csrf 방어기능 disable
			.headers().frameOptions().disable() // frame, iframe 통한 공격 방어 가능. h2 console이 iframe 형식이므로 disable
			.and()
			.authorizeRequests() // Url 별 권한 관리 설정 진입점
			.antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // 해당 url 모두 접근 가능. 기본 페이지와 js, css 등
			.antMatchers("/api/v1/**").hasRole(Role.USER.name()) // api는 Role이 User인 사람들만 접근가능
			.anyRequest().authenticated() // 위에 명시되지 않은 나머지 요청들 처리. 인증된 사용자(로그인된 사용자)만 접근 가능
			.and()
			.logout() // 로그아웃 관련 설정 진입점
			.logoutSuccessUrl("/") // 로그아웃 성공시 /로 이동
			.and()
			.oauth2Login()// oAuth2 로그인 기능 설정 진입점
			.userInfoEndpoint() // 로그인 성공 이후 처리 설정
			.userService(customOauth2UserService); // 성공 이후 후속처리 userService 구현체에서 수행하도록 설정
	}
}
