package com.acme.blog.boundary;

import com.acme.blog.domain.comment.CommentDto;
import com.acme.blog.domain.comment.CommentService;
import com.acme.blog.domain.comment.NewCommentDto;
import com.acme.blog.domain.post.PostDto;
import com.acme.blog.domain.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    public PostController(final PostService postService, final CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public Optional<PostDto> getPost(@PathVariable Long id) {
        return postService.getPost(id)
                .map(Optional::of)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("/{id}/comments")
    public List<CommentDto> getComments(@PathVariable Long id) {
        return commentService.getCommentsForPost(id);
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public void addComment(@PathVariable Long id, @RequestBody NewCommentDto newCommentDto) {
        commentService.addComment(newCommentDto);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    static class ResourceNotFoundException extends RuntimeException {
    }
}
