package com.acme.blog.domain.comment;

import lombok.Data;

@Data
public class NewCommentDto {

	private Long postId;
	private String author;
	private String content;

}
