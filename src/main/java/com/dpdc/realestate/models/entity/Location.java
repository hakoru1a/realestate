package com.dpdc.realestate.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "location", schema = "realestate")
public class Location {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 255)
    @Column(name = "state")
    private String state;

    @Size(max = 255)
    @Column(name = "country")
    private String country;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

}