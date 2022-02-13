package com.acme.blog.domain.post;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostServiceTest {

	@Autowired
	PostRepository postRepository;

	@Autowired
	PostService postService;

	@Test
	public void shouldReturnCreatedPost() {
		Post post = new Post();
		post.setTitle("Test title");
		post.setContent("Test content");
		LocalDate creationDate = LocalDate.of(2019, 12, 13);
		post.setCreationDate(creationDate);
		postRepository.save(post);

		Optional<PostDto> postDto = postService.getPost(post.getId());

		assertThat(postDto).isPresent();
		postDto.ifPresent(dto -> {
			assertThat(dto).extracting(PostDto::getContent).isEqualTo("Test content");
			assertThat(dto).extracting(PostDto::getTitle).isEqualTo("Test title");
			assertThat(dto).extracting(PostDto::getCreationDate).isEqualTo(creationDate);
		});

	}

	@Test
	public void shouldReturnNullForNotExistingPost() {
		Optional<PostDto> postDto = postService.getPost(123L);

		assertThat(postDto).isEmpty();
	}
}
