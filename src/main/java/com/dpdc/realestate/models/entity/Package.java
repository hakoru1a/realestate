package com.dpdc.realestate.models.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "package", schema = "realestate")
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotEmpty
    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "times", nullable = false)
    private Integer times;

//    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal price;

    @Column(name = "created_at", insertable = false)
    private Instant createdAt;

    @Column(name = "modified_at", insertable = false)
    private Instant modifiedAt;

}