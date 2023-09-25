package com.dpdc.realestate.repository;

import com.dpdc.realestate.models.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Page<Payment> findByCustomerPackageRegistration_CustomerId(Integer customerId, Pageable pageable);
}
