package com.dpdc.realestate.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "blog", schema = "realestate", indexes = {
        @Index(name = "user_id", columnList = "user_id")
})
public class Blog {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "publish_date")
    private Instant publishDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

}