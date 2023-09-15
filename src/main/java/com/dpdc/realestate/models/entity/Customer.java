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
@Table(name = "customer", schema = "realestate")
public class Customer {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 255)
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "TIMESTAMP_of_birth")
    private Instant timestampOfBirth;

    @Lob
    @Column(name = "gender")
    private String gender;

    @Lob
    @Column(name = "address")
    private String address;

    @Lob
    @Column(name = "avatar")
    private String avatar;

    @Size(max = 255)
    @Column(name = "occupation")
    private String occupation;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

}