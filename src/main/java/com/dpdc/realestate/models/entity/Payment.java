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
@Table(name = "payment", schema = "realestate", indexes = {
        @Index(name = "transaction_id", columnList = "transaction_id")
})
public class Payment {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Column(name = "payment_date", nullable = false)
    private Instant paymentDate;

    @Size(max = 255)
    @Column(name = "payment_method")
    private String paymentMethod;

    @Size(max = 255)
    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

}