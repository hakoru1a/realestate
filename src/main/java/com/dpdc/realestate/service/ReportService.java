package com.dpdc.realestate.service;

import com.dpdc.realestate.dto.report.AppointmentReport;
import com.dpdc.realestate.dto.report.PackageReport;
import com.dpdc.realestate.dto.report.PropertyTypeReport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

public interface ReportService {


    List<PackageReport> countQuantityPackage(Instant from, Instant to);

    List<PropertyTypeReport> countTypeOfProperty(Instant from, Instant to);
    BigInteger countUserRegister(Instant from, Instant to);

    List<AppointmentReport> countDayAppointment(int month, int year);

    BigDecimal calculateTotalRevenueInDateRange(Instant from, Instant to);
}
