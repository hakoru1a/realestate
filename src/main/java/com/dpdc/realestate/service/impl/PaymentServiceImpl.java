package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.dto.response.BillDTO;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.exception.ForbiddenException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.CustomerPackageRegistration;
import com.dpdc.realestate.models.entity.Payment;
import com.dpdc.realestate.models.entity.PaymentData;
import com.dpdc.realestate.models.enumerate.PaymentType;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.repository.PaymentRepository;
import com.dpdc.realestate.repository.RegisterPackageRepository;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RegisterPackageRepository registerPackageRepository;


    @Override
    public Payment createPayment(Integer packageId, Integer customerId,  Integer quantity) {
        BillDTO billDTO = registerPackageService.checkBill(packageId, customerId, quantity);
        Customer customer = EntityCheckHandler.checkEntityExistById(customerRepository, customerId);
        CustomerPackageRegistration customerPackageRegistration = registerPackageService
                .savePackageRegister(packageId, customerId, quantity);
        if (billDTO != null){
            Payment payment = new Payment();
            payment.setPaymentDate(Instant.now());
            payment.setPaymentMethod("MOMO");
            payment.setAmount(customerPackageRegistration.getServicePackage()
                    .getPrice().multiply(BigDecimal.valueOf(quantity)));
            payment.setType(PaymentType.BUY_TURN.name());
            payment.setCustomer(customer);
            payment.setPaymentStatus("PAID");
            Payment pay =  paymentRepository.save(payment);
            customerPackageRegistration.setPayment(payment);
            return pay;
        }
        throw new BodyBadRequestException(env.getProperty("api.notify.bad_request"));
    }

    @Override
    public Set<PaymentData> getPayments(Integer customerId) {
        isMyPaymentOrAdmin(customerId);
        return paymentRepository.findPaymentsByPaymentTypeAndCustomerId(PaymentType.BUY_TURN.name(), customerId);
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
