package com.dpdc.realestate.repository;

import com.dpdc.realestate.dto.report.AppointmentReport;
import com.dpdc.realestate.dto.report.PackageReport;
import com.dpdc.realestate.dto.report.PropertyTypeReport;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

public interface ReportRepository {

    List<PackageReport> countQuantityPackage(Instant from, Instant to);
    // Đếm số lượng gọi được mua trong khoảng thời gian
//    [item1, item2, item3] // Tương ứng với mỗi gói
    BigInteger countUserRegister(Instant from, Instant to);

    List<PropertyTypeReport> countTypeOfProperty(Instant from, Instant to);

    List<AppointmentReport> countDayAppointment(int day, int year);

    BigDecimal calculateTotalRevenueInDateRange( Instant fromDate, Instant toDate);
}
