package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Appointment;
import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.models.entity.PaymentData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface PaymentService {

    Payment createPaymentFromBuyTurn( Integer packageId,Integer customerId, Integer quantity);

    Payment createPaymentFromBookAppointment(Appointment appointment);

    Set<PaymentData> getPayments(Integer customerId );

    void isValidAppointment(Appointment appointment);
}
