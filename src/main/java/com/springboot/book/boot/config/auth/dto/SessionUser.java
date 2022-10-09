package com.springboot.book.boot.config.auth.dto;

import java.io.Serializable;

import com.springboot.book.boot.domain.user.User;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable { // 로그인한 User를 세션에 저장하는데 활용하는 dto. 세션에 저장하려면 직렬화 필요해 별도 클래스 구축.
	private String name;
	private String email;
	private String picture;

	public SessionUser(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.picture = user.getPicture();
	}
}
