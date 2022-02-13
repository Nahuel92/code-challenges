package com.acme.blog.boundary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.acme.blog.domain.comment.CommentService;
import com.acme.blog.domain.comment.CommentDto;
import com.acme.blog.domain.comment.NewCommentDto;
import com.acme.blog.domain.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PostController.class)
public class CommentsRestTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CommentService commentService;

	@MockBean
	private PostService postService;

	@Test
	public void shouldReturnFoundComments() throws Exception {

		// given
		List<CommentDto> comments = new ArrayList<CommentDto>();
		LocalDate creationDate = LocalDate.of(2012, 1, 1);
		comments.add(new CommentDto(2L, "comment content", "John Smith", creationDate));

		// when
		when(commentService.getCommentsForPost(1L)).thenReturn(comments);

		// then
		mockMvc.perform(get("/post/1/comments").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(2)))
				.andExpect(jsonPath("$[0].comment", is("comment content")))
				.andExpect(jsonPath("$[0].author", is("John Smith")))
				.andExpect(jsonPath("$[0].creationDate", is(creationDate.toString())));
	}

	@Test
	public void shouldAddComment() throws Exception {

		// given
		NewCommentDto newComment = createComment("Test content", "John Doe");

		// when
		when(commentService.addComment(newComment)).thenReturn(1L);

		// then
		mockMvc.perform(post("/post/1/comment")
				.content(objectMapper.writeValueAsBytes(newComment))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}

	private NewCommentDto createComment(String content, String author) {
		NewCommentDto newComment = new NewCommentDto();
		newComment.setContent(content);
		newComment.setAuthor(author);
		return newComment;
	}

}
