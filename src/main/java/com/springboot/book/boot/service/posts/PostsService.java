package com.springboot.book.boot.service.posts;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.book.boot.domain.posts.Posts;
import com.springboot.book.boot.domain.posts.PostsRepository;
import com.springboot.book.boot.web.dto.PostsListResponseDto;
import com.springboot.book.boot.web.dto.PostsResponseDto;
import com.springboot.book.boot.web.dto.PostsSaveRequestDto;
import com.springboot.book.boot.web.dto.PostsUpdateRequestDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostsService {
	private final PostsRepository postsRepository;

	public PostsResponseDto findById(Long id) {
		Posts entity = postsRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));
		return new PostsResponseDto(entity);
	}

	@Transactional(readOnly = true)
	public List<PostsListResponseDto> findAllDesc() {
		// findAllDesc 결과인 Posts의 Stream을 map을 통해 PostsListResponseDto로 변환 후 List로 반환
		return postsRepository.findAllDesc().stream()
			.map(PostsListResponseDto::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public Long save(PostsSaveRequestDto requestDto) {
		return postsRepository.save(requestDto.toEntity()).getId();
	}

	@Transactional
	public Long update(Long id, PostsUpdateRequestDto requestDto) {
		// Posts DB에서 조회. 영속성 컨텍스트로 들어감
		Posts posts = postsRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

		// 조회한 Posts 수정. flush로 DB에 반영됨.
		posts.update(requestDto.getTitle(), requestDto.getContent());
		return id;
	}

	@Transactional
	public void delete (Long id) {
		Posts posts = postsRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

		postsRepository.delete(posts);
	}
}
