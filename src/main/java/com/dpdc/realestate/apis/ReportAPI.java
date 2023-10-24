package com.dpdc.realestate.apis;

import com.dpdc.realestate.dto.ModelResponse;
import com.dpdc.realestate.dto.report.AppointmentReport;
import com.dpdc.realestate.dto.report.PackageReport;
import com.dpdc.realestate.dto.report.PropertyTypeReport;
import com.dpdc.realestate.service.ReportService;
import org.modelmapper.spi.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.Month;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report/")
public class ReportAPI {

    @Autowired
    private ReportService reportService;

    @Autowired
    private Environment env;

    @GetMapping("/package-quantity/")
    public ResponseEntity<ModelResponse> countQuantityPackage(){
        Year currentYear = Year.now();
        Instant startOfYear = currentYear.atMonth(Month.JANUARY).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfYear = currentYear.atMonth(Month.DECEMBER).atEndOfMonth().atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();
        List<PackageReport> reports = reportService.countQuantityPackage(startOfYear,endOfYear);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                reports), HttpStatus.OK);
    }
    @GetMapping("/property-type/")
    public ResponseEntity<ModelResponse> getPackagePurchaseStats(){
        Year currentYear = Year.now();
        Instant startOfYear = currentYear.atMonth(Month.JANUARY).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfYear = currentYear.atMonth(Month.DECEMBER).atEndOfMonth().atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();
        List<PropertyTypeReport> report = reportService.countTypeOfProperty(startOfYear, endOfYear);
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                report), HttpStatus.OK);
    }
    @GetMapping("/user-register/")
    public ResponseEntity<ModelResponse> getAppointmentStatsForNextWeek(){
        Year currentYear = Year.now();
        Instant startOfYear = currentYear.atMonth(Month.JANUARY).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfYear = currentYear.atMonth(Month.DECEMBER).atEndOfMonth().atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();
        BigInteger report = reportService.countUserRegister(startOfYear,endOfYear );
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                report), HttpStatus.OK);
    }

    @GetMapping("/appointment-weekly/")
    public ResponseEntity<ModelResponse> countDayAppointment(){
        List<AppointmentReport> reports = reportService.countDayAppointment(10,2023 );
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                reports), HttpStatus.OK);
    }
    @GetMapping("/total-payment/")
    public ResponseEntity<ModelResponse> calculateTotalRevenueInDateRange(){
        Year currentYear = Year.now();
        Instant startOfYear = currentYear.atMonth(Month.JANUARY).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfYear = currentYear.atMonth(Month.DECEMBER).atEndOfMonth().atTime(23, 59, 59).atZone(ZoneOffset.UTC).toInstant();
        BigDecimal reports = reportService.calculateTotalRevenueInDateRange(startOfYear,endOfYear );
        return new
                ResponseEntity<>(new ModelResponse(env.getProperty("api.notify.success"),
                reports), HttpStatus.OK);
    }
}
