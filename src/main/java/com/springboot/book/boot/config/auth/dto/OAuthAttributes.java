package com.springboot.book.boot.config.auth.dto;

import java.util.Map;

import com.springboot.book.boot.domain.user.Role;
import com.springboot.book.boot.domain.user.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes { // OAuth2UserService를 통해 가져온 OAuth2User를 담을 dto. 구글과 네이버로그인에 공통으로 사용
	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String picture;

	@Builder
	public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.picture = picture;
	}

	public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
		if("naver".equals(registrationId)) {
			return ofNaver("id", attributes);
		}

		return ofGoogle(userNameAttributeName, attributes);
	}

	private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
		Map<String, Object> response = (Map<String, Object>) attributes.get("response");
		return OAuthAttributes.builder()
			.name((String) response.get("name"))
			.email((String) response.get("email"))
			.picture((String) response.get("profile_image"))
			.attributes(response)
			.nameAttributeKey(userNameAttributeName)
			.build();
	}
	private static OAuthAttributes ofGoogle(String userNameAttributesName, Map<String, Object> attributes) {
		return OAuthAttributes.builder()
			.name((String) attributes.get("name"))
			.email((String) attributes.get("email"))
			.picture((String) attributes.get("picture"))
			.attributes(attributes)
			.nameAttributeKey(userNameAttributesName)
			.build();
	}

	public User toEntity() {
		return User.builder()
			.name(name)
			.email(email)
			.picture(picture)
			.role(Role.GUEST)
			.build();
	}
}
