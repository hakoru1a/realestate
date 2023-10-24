package com.dpdc.realestate.repository.impl;


import com.dpdc.realestate.dto.report.AppointmentReport;
import com.dpdc.realestate.dto.report.PackageReport;
import com.dpdc.realestate.dto.report.PropertyTypeReport;
import com.dpdc.realestate.repository.ReportRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<PackageReport> countQuantityPackage(Instant from, Instant to) {
        String sql = "SELECT p.package_name AS name, COALESCE(COUNT(cpr.id), 0) AS value " +
                "FROM Package p " +
                "LEFT JOIN CustomerPackageRegistration cpr ON p.id = cpr.package_id " +
                "AND (cpr.registration_date >= :fromDate OR :fromDate IS NULL) " +
                "AND (cpr.registration_date <= :toDate OR :toDate IS NULL) " +
                "GROUP BY p.package_name";


        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("fromDate", from)
                .setParameter("toDate", to)
                .getResultList();
        List<PackageReport> responses = new ArrayList<>();
        for (Object[] result : results) {
            String name = (String) result[0];
            Long value = ((Number) result[1]).longValue(); // Cast to Long
            PackageReport response = new PackageReport( name, value);
            responses.add(response);
        }

        return responses;
    }

    @Override
    public BigInteger countUserRegister(Instant from, Instant to) {
        String sql = "SELECT COUNT(u.id) " +
                "FROM User u " +
                "WHERE u.created_at >= :fromDate AND u.created_at <= :toDate";

        return (BigInteger) entityManager.createNativeQuery(sql)
                .setParameter("fromDate", from)
                .setParameter("toDate", to)
                .getSingleResult();
    }

    @Override
    public List<PropertyTypeReport> countTypeOfProperty(Instant from, Instant to) {
        String sql = "SELECT 'FOR_RENT' AS purpose, COALESCE(COUNT(CASE WHEN p.purpose = 'FOR_RENT' THEN p.id END), 0) AS value " +
                "FROM Property p " +
                "WHERE p.created_at >= :fromDate AND p.created_at <= :toDate " +
                "UNION " +
                "SELECT 'FOR_SALE' AS purpose, COALESCE(COUNT(CASE WHEN p.purpose = 'FOR_SALE' THEN p.id END), 0) AS value " +
                "FROM Property p " +
                "WHERE p.created_at >= :fromDate AND p.created_at <= :toDate";

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("fromDate", from)
                .setParameter("toDate", to)
                .getResultList();
        List<PropertyTypeReport> responses = new ArrayList<>();
        for (Object[] result : results) {
            String purpose = (String) result[0];
            Long value = ((Number) result[1]).longValue(); // Cast to Long
            PropertyTypeReport response = new PropertyTypeReport(purpose, value);
            responses.add(response);
        }

        return responses;
    }

    @Override
    public List<AppointmentReport> countDayAppointment(int month, int year) {
        String sql = "WITH days AS ( " +
                "SELECT 'Monday' AS DAY " +
                "UNION ALL SELECT 'Tuesday' " +
                "UNION ALL SELECT 'Wednesday' " +
                "UNION ALL SELECT 'Thursday' " +
                "UNION ALL SELECT 'Friday' " +
                "UNION ALL SELECT 'Saturday' " +
                "UNION ALL SELECT 'Sunday' " +
                ") " +
                "SELECT days.DAY AS DAY, COALESCE(COUNT(appointment.appointment_date), 0) AS Count " +
                "FROM days " +
                "LEFT JOIN appointment ON DAYNAME(appointment.appointment_date) = days.DAY " +
                "AND YEAR(appointment.appointment_date) = :year " +
                "AND MONTH(appointment.appointment_date) = :month " +
                "GROUP BY days.DAY";

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
        List<AppointmentReport> responses = new ArrayList<>();
        for (Object[] result : results) {
            String dayOfWeek = (String) result[0];
            BigInteger count = (BigInteger) result[1]; // Cast to BigInteger
            AppointmentReport response = new AppointmentReport(dayOfWeek, count);
            responses.add(response);
        }

        return responses;
    }

    @Override
    public BigDecimal calculateTotalRevenueInDateRange(Instant fromDate, Instant toDate) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Payment " +
                "WHERE payment_date >= :fromDate AND payment_date <= :toDate";

        BigDecimal totalRevenue = (BigDecimal) entityManager.createNativeQuery(sql)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .getSingleResult();
        return totalRevenue;
    }
}
