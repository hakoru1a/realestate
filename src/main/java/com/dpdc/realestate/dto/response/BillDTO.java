package com.dpdc.realestate.dto.response;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Package;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class BillDTO {
    private Customer customer;
    private Package pack;
    private Integer quantity;
    private Instant createdAt;
}
