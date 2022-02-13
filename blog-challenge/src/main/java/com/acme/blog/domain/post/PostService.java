package com.acme.blog.domain.post;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {

	@Autowired
	private PostRepository postRepository;

	/**
	 * Returns a blog post for passed id.
	 *
	 * @param id id of the post
	 * @return post data or null if no post is found for passed id
	 */
	public Optional<PostDto> getPost(Long id) {
		return postRepository.findById(id)
				.map(post -> new PostDto(post.getTitle(), post.getContent(), post.getCreationDate()));
	}
}
