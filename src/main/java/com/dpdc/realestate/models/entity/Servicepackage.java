package com.dpdc.realestate.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "servicepackage", schema = "realestate")
public class Servicepackage {
    @Id
    @Column(name = "package_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @NotNull
    @Column(name = "duration_in_months", nullable = false)
    private Integer durationInMonths;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

}