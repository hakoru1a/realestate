package com.dpdc.realestate.models.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class PaymentData {

    private BigDecimal paymentAmount;
    private String paymentStatus;
    private Instant paymentDate;
    private Integer packageId;
    private Integer quantity;
}
