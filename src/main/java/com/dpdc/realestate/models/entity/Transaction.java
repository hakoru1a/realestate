package com.dpdc.realestate.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "transaction", schema = "realestate", indexes = {
        @Index(name = "property_id", columnList = "property_id"),
        @Index(name = "customer_id", columnList = "customer_id")
})
public class Transaction {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "transaction_date")
    private Instant transactionDate;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

}