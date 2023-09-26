package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.models.entity.PaymentData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Page<Payment> findByCustomerId(Integer customerId, Pageable pageable);

    @Query("SELECT new com.dpdc.realestate.models.entity.PaymentData(p.amount, " +
            "p.paymentStatus, " +
            "p.paymentDate, " +
            "cp.servicePackage.id, " +
            "cp.quantity) " +
            "FROM Payment p " +
            "JOIN CustomerPackageRegistration cp ON cp.payment.id = p.id " +
            "WHERE p.type = :paymentType " +
            "AND p.customer.id = :customerId")
    Set<PaymentData> findPaymentsByPaymentTypeAndCustomerId(@Param("paymentType") String paymentType,
                                                             @Param("customerId") Integer customerId);
}
