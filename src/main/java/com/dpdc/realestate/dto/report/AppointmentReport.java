package com.dpdc.realestate.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentReport {
    private String day;
    private BigInteger value;
}
