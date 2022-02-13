package com.acme.blog.domain.comment;

import com.acme.blog.domain.post.PostService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommentService {
	private final PostService postService;
	private final CommentRepository commentRepository;

	public CommentService(final PostService postService, final CommentRepository commentRepository) {
		this.postService = postService;
		this.commentRepository = commentRepository;
	}

	/**
	 * Returns a list of all comments for a blog post with passed id.
	 *
	 * @param postId id of the post
	 * @return list of comments sorted by creation date descending - most recent first
	 * @throws IllegalArgumentException if there is no blog post for passed postId
	 */
	public List<CommentDto> getCommentsForPost(final Long postId) {
		Objects.requireNonNull(postId, "PostId can not be null");
		checkPostExistance(postId);

		return commentRepository.findAllByPostId(postId)
				.stream()
				.map(c -> new CommentDto(c.getId(), c.getComment(), c.getAuthor(), c.getCreationDate()))
				.collect(Collectors.toList());
	}

	/**
	 * Creates a new comment
	 *
	 * @param newCommentDto data of new comment
	 * @return id of created comment
	 * @throws IllegalArgumentException if there is no blog post for passed newCommentDto.postId
	 */
	public Long addComment(final NewCommentDto newCommentDto) {
		Objects.requireNonNull(newCommentDto, "NewCommentDto can not be null");
		Objects.requireNonNull(newCommentDto.getPostId(), "PostId can not be null");
		checkPostExistance(newCommentDto.getPostId());

		final Comment comment = entityFrom(newCommentDto);
		commentRepository.save(comment);
		return comment.getId();
	}

	private void checkPostExistance(final Long postId) {
		postService.getPost(postId).orElseThrow(IllegalArgumentException::new);
	}

	private Comment entityFrom(final NewCommentDto dto) {
		return new Comment(dto.getPostId(), dto.getContent(), dto.getAuthor(), LocalDate.now());
	}
}
