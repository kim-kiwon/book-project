package com.springboot.book.boot.web.dto;

import com.springboot.book.boot.domain.posts.Posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {
	private String title;
	private String content;
	private String author;

	// 생성자 대신 빌더
	@Builder
	public PostsSaveRequestDto(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}

	// Entity화 하는 toEntity 메소드
	public Posts toEntity() {
		return Posts.builder()
			.title(title)
			.content(content)
			.author(author)
			.build();
	}
}
