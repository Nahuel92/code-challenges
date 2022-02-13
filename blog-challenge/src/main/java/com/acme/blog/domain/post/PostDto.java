package com.acme.blog.domain.post;

import java.time.LocalDate;

import lombok.Value;

@Value
public class PostDto {

	private final String title;
	private final String content;
	private final LocalDate creationDate;

}
