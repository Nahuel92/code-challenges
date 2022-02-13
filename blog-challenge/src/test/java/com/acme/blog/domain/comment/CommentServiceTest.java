package com.acme.blog.domain.comment;

import java.time.LocalDate;
import java.util.List;

import com.acme.blog.domain.post.Post;
import com.acme.blog.domain.post.PostRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentService commentService;

    @Test
    public void shouldAddComment() {
        Post post = createTestPost();

        NewCommentDto comment = new NewCommentDto();
        comment.setPostId(post.getId());
        comment.setAuthor("Author");
        comment.setContent("Content");
        Long commentId = commentService.addComment(comment);

        assertThat(commentId).isNotNull();
    }

    @Test
    public void shouldReturnAddedComment() {
        Post post = createTestPost();

        NewCommentDto comment = new NewCommentDto();
        comment.setPostId(post.getId());
        comment.setAuthor("Author");
        comment.setContent("Content");

        commentService.addComment(comment);

        List<CommentDto> comments = commentService.getCommentsForPost(post.getId());

        assertThat(comments).hasSize(1);
        assertThat(comments).first()
                .extracting(CommentDto::getAuthor).isEqualTo("Author");
        assertThat(comments).first()
                .extracting(CommentDto::getComment).isEqualTo("Content");
    }

    private Post createTestPost() {
        Post post = new Post();
        post.setTitle("Test title");
        post.setContent("Test content");
        LocalDate creationDate = LocalDate.of(2012, 1, 2);
        post.setCreationDate(creationDate);
        postRepository.save(post);
        return post;
    }
}
