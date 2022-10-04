package com.springboot.book.boot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsUpdateRequestDto {
	private String title;
	private String content;

	// 생성자 대신 빌더
	@Builder
	public PostsUpdateRequestDto(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
