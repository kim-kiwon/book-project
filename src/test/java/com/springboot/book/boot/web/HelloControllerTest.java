package com.springboot.book.boot.web;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.springboot.book.boot.config.auth.SecurityConfig;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class, excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
	// @WebMvcTest는 @WebSecurityConfigurerAdapter 와 WebMvcConfigurer를 읽지만, @Service를 안읽음.
	// SecurityConfig 생성을 위한 CustomOAuth2UserService 르 읽지 않아 에러 발생. 그래서 제외하도록 필터 추가함.

	// @JpaAuditing이 Applicaiton 자체에 있으면 오류 발생. @JpaAuditing은 최소 하나이상 @Entity가 필수인데, @WebMvcTest는 @Entity는 로딩하지 않으므로.
	// 그래서 JpaConfig로 이를 분리.
})
public class HelloControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	@WithMockUser(roles = "USER")
	public void hello가_리턴된다() throws Exception {
		String hello = "hello";

		mvc.perform(get("/hello"))
			.andExpect(status().isOk())
			.andExpect(content().string(hello));
	}

	@Test
	@WithMockUser(roles = "USER")
	public void helloDto가_리턴된다() throws Exception {
		String name = "hello";
		int amount = 1000;

		mvc.perform(
			get("/hello/dto")
				.param("name", name)
				.param("amount", String.valueOf(amount)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is(name)))
			.andExpect(jsonPath("$.amount", is(amount)));
	}
}