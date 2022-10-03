package com.springboot.book.boot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.book.boot.web.dto.HelloResponseDto;

@RestController
public class HelloController {
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}

	@GetMapping("/hello/dto")
	public HelloResponseDto helloDto(String name, int amount) {
		return new HelloResponseDto(name, amount);
	}
}
