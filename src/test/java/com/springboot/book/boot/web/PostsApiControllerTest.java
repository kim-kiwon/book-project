package com.springboot.book.boot.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.book.boot.domain.posts.Posts;
import com.springboot.book.boot.domain.posts.PostsRepository;
import com.springboot.book.boot.web.dto.PostsSaveRequestDto;
import com.springboot.book.boot.web.dto.PostsUpdateRequestDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private PostsRepository postsRepository;

	@After
	public void tearDown() throws Exception {
		postsRepository.deleteAll();
	}

	@Autowired
	private WebApplicationContext context;

	private MockMvc mvc;

	@Before
	public void setup() {
		// 매 테스트 시작전 Mock mvc 생성
		mvc = MockMvcBuilders
			.webAppContextSetup(context)
			.apply(springSecurity())
			.build();
	}

	@Test
	@WithMockUser(roles="USER") // 테스트를 위한 가상의 User는 MockMVC에서만 동작
	public void Posts_등록된다() throws Exception {
		//given
		String title = "title";
		String content = "content";
		PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
			.title(title)
			.content(content)
			.author("author")
			.build();

		String url = "http://localhost:" + port + "/api/v1/posts";

		//when
		mvc.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isOk());

		//then
		List< Posts> all = postsRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(title);
		assertThat(all.get(0).getContent()).isEqualTo(content);
	}

	@Test
	@WithMockUser(roles="USER")

	public void Posts_수정된다() throws Exception {
		//given
		Posts savedPosts = postsRepository.save(Posts.builder()
			.title("title")
			.content("content")
			.author("author")
			.build());

		Long updateId = savedPosts.getId();
		String expectedTitle = "title2";
		String expectedContent = "content2";

		PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
			.title(expectedTitle)
			.content(expectedContent)
			.build();

		String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

		HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

		//when
		mvc.perform(put(url)
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(requestDto)))
			.andExpect(status().isOk());

		//then
		List<Posts> all = postsRepository.findAll();
		assertThat(all.get(0).getTitle()).isEqualTo(expectedTitle);
		assertThat(all.get(0).getContent()).isEqualTo(expectedContent);
	}
}
