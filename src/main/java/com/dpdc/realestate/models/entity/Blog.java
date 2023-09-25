package com.dpdc.realestate.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "is_publish", insertable = false)
    private Boolean isPublish;

    @NotNull
    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "publish_date", insertable = false)
    private Instant publishDate;

    @Column(name = "created_at", insertable = false)
    private Instant createdAt;

    @Column(name = "modified_at", insertable = false)
    private Instant modifiedAt;

}