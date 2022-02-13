package com.acme.blog.domain.comment;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CommentRepository extends CrudRepository<Comment, Long> {
    Collection<Comment> findAllByPostId(final Long postId);
}
