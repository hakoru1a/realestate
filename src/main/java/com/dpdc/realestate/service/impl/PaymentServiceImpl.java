package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.dto.response.BillDTO;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.CustomerPackageRegistration;
import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.repository.PaymentRepository;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.PaymentService;
import com.dpdc.realestate.service.RegisterPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RegisterPackageService registerPackageService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private Environment env;

    @Autowired
    private CustomerService customerService;

    @Override
    public Payment createPayment(Integer packageId, Integer customerId,  Integer quantity) {
        BillDTO billDTO = registerPackageService.checkBill(packageId, customerId, quantity);
        CustomerPackageRegistration customerPackageRegistration = registerPackageService
                .savePackageRegister(packageId, customerId, quantity);
        if (billDTO != null){
            Payment payment = new Payment();
            payment.setPaymentDate(Instant.now());
            payment.setPaymentMethod("MOMO");
            payment.setAmount(customerPackageRegistration.getServicePackage()
                    .getPrice().multiply(BigDecimal.valueOf(quantity)));
            payment.setCustomerPackageRegistration(customerPackageRegistration);
            payment.setPaymentStatus("PAID");
            return  paymentRepository.save(payment);
        }
        throw new BodyBadRequestException(env.getProperty("api.notify.bad_request"));
    }

    @Override
    public Page<Payment> getPayments(Integer customerId, Pageable pageable) {
        isMyPaymentOrAdmin(customerId);
        return paymentRepository.findByCustomerPackageRegistration_CustomerId(customerId, pageable);
    }
    private void  isMyPaymentOrAdmin(Integer customerId){
        Customer currentCustomer = customerService.getCurrentCredential();
        if(currentCustomer != null){
            if (!Objects.equals(currentCustomer.getId(), customerId)){
                throw new ForbiddenException("Access denied: Comment does not belong to the current customer");
            }
        }
    }
}
