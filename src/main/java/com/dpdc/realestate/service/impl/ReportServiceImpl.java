package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.dto.report.AppointmentReport;
import com.dpdc.realestate.dto.report.PackageReport;
import com.dpdc.realestate.dto.report.PropertyTypeReport;
import com.dpdc.realestate.repository.ReportRepository;
import com.dpdc.realestate.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Override
    public List<PackageReport> countQuantityPackage(Instant from, Instant to) {
        return reportRepository.countQuantityPackage(from, to);
    }

    @Override
    public List<PropertyTypeReport> countTypeOfProperty(Instant from, Instant to) {
        return reportRepository.countTypeOfProperty(from, to);
    }

    @Override
    public BigInteger countUserRegister(Instant from, Instant to) {
        return reportRepository.countUserRegister(from, to);
    }

    @Override
    public List<AppointmentReport> countDayAppointment(int month, int year) {
        return reportRepository.countDayAppointment(month, year);
    }

    @Override
    public BigDecimal calculateTotalRevenueInDateRange(Instant from, Instant to) {
        return reportRepository.calculateTotalRevenueInDateRange(from, to);
    }
}
