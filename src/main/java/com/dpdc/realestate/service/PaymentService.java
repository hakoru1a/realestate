package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.models.entity.PaymentData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface PaymentService {

    Payment createPayment( Integer packageId,Integer customerId, Integer quantity);

    Set<PaymentData> getPayments(Integer customerId );
}
