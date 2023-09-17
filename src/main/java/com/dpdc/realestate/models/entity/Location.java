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
@Table(name = "location", schema = "realestate")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull(message = "Thành phố là bắt buộc")
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 255)
    @NotNull(message = "Quận là bắt buộc")
    @Column(name = "district")
    private String district;

    @Size(max = 255)
    @NotNull(message = "Đường là bắt buộc")
    @Column(name = "street")
    private String street;



}