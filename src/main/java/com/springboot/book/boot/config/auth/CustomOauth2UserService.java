package com.springboot.book.boot.config.auth;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.springboot.book.boot.config.auth.dto.OAuthAttributes;
import com.springboot.book.boot.config.auth.dto.SessionUser;
import com.springboot.book.boot.domain.user.User;
import com.springboot.book.boot.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final UserRepository userRepository;
	private final HttpSession httpSession;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(userRequest);

		String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 현재 로그인처리 하고있는 서비스 구분 Id. 구글 로그인인지 네이버 로그인 인지.
		String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // 로그인 진행의 유니크한 키가 되는 값. 구글거는 "google"

		OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes()); // OAuthattributes 로그인 처리중인 oAuthUser의 attribute를 저장
		// registrantionId : "google"
		// userNameAttributesNAme : "sub" : 구글에서 제공
		// oAuth2User.getAttributes : 구글 로그인 관련 속성들. 이름, 프로픨 사진, 이메일, 이메일 검증 여부, 지역 등

		User user = saveOrUpdate(attributes);
		httpSession.setAttribute("user", new SessionUser(user)); // User 엔티티를 SessionUser라는 Dto로 변경해 sesison에 저장

		return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())), attributes.getAttributes(), attributes.getNameAttributeKey());

	}

	private User saveOrUpdate(OAuthAttributes attributes) {
		User user = userRepository.findByEmail(attributes.getEmail()) // 해당 Email이 존재한다면 수정한 entity를. 아니라면 toEntity화만.
			.map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
			.orElse(attributes.toEntity());

		return userRepository.save(user);
	}
}
