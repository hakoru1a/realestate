package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.CustomerPackageRegistration;
import com.dpdc.realestate.models.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RegisterPackageRepository extends JpaRepository<CustomerPackageRegistration, Integer> {

    @Query("SELECT SUM(cpr.quantity) " +
            "FROM CustomerPackageRegistration cpr " +
            "WHERE cpr.customer.id = :customerId " +
            "AND cpr.servicePackage.id <> 1 " +
            "AND cpr.registrationDate >= :startDate " +
            "AND cpr.registrationDate <= :endDate ")
    Integer sumQuantityByCustomerIdAndDateRange(@Param("customerId") Integer customerId,
                                                @Param("startDate") Instant startDate,
                                                @Param("endDate") Instant endDate);

    CustomerPackageRegistration findByIdAndServicePackageAndRegistrationDateBetween(
            Integer id, Package servicePackage, Instant startDate, Instant endDate
    );

    CustomerPackageRegistration findByCustomerAndServicePackageAndRegistrationDateBetween
            (Customer customer, Package aPackage,
             Instant startDateOfCurrentMonth, Instant endDateOfCurrentMonth);
}
