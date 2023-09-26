package com.dpdc.realestate.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "customerpackageregistration", schema = "realestate", indexes = {
        @Index(name = "customer_id", columnList = "customer_id"),
        @Index(name = "id", columnList = "id")
})
public class CustomerPackageRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    private Package servicePackage;

    @JsonIgnore
    @Column(name = "quantity")
    private Integer quantity;


    @Column(name = "registration_date",  insertable = false, updatable = false)
    private Instant registrationDate;




}