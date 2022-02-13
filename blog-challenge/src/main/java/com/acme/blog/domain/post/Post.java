package com.acme.blog.domain.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

import lombok.Data;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(length = 4096)
    private String content;

    private LocalDate creationDate;

}
