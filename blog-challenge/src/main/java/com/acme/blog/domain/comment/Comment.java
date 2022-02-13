package com.acme.blog.domain.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Long id;
    private Long postId;
    private String comment;
    private String author;
    private LocalDate creationDate;

    public Comment(Long postId, String comment, String author, LocalDate creationDate) {
        this.postId = postId;
        this.comment = comment;
        this.author = author;
        this.creationDate = creationDate;
    }
}
