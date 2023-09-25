package com.dpdc.realestate.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "comment", schema = "realestate", indexes = {
        @Index(name = "customer_id", columnList = "customer_id"),
        @Index(name = "property_id", columnList = "property_id")
})
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @NotEmpty(message = "comment text not empty")
    @Lob
    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at", insertable = false)
    private Instant createdAt;

    @Column(name = "modified_at", insertable = false)
    private Instant modifiedAt;



}