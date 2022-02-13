package com.acme.blog.domain.comment;

import lombok.Value;

import java.time.LocalDate;

@Value
public class CommentDto {
	private final Long id;
	private final String comment;
	private final String author;
	private final LocalDate creationDate;
}
