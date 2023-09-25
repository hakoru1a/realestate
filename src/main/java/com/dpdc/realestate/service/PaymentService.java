package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {

    Payment createPayment( Integer packageId,Integer customerId, Integer quantity);

    Page<Payment> getPayments( Integer customerId ,Pageable pageable);
}
