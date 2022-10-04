package com.springboot.book.boot.domain.posts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.springboot.book.boot.domain.BaseTimeEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 500, nullable = false)
	private String title;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String content;

	private String author;

	@Builder
	private Posts(String title, String content, String author) {
		this.title = title;
		this.content = content;
		this.author = author;
	}

	// 도메인 중심 설계
	// update 메소드 service가 아닌 도메인이 가짐
	public void update(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
