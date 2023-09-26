package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.dto.response.BillDTO;
import com.dpdc.realestate.exception.BodyBadRequestException;
import com.dpdc.realestate.handler.EntityCheckHandler;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.CustomerPackageRegistration;
import com.dpdc.realestate.models.entity.Package;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.repository.PackageRepository;
import com.dpdc.realestate.repository.PaymentRepository;
import com.dpdc.realestate.repository.RegisterPackageRepository;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.RegisterPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.*;

@Service
@Transactional
public class RegisterPackageServiceImpl implements RegisterPackageService {

    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RegisterPackageRepository registerPackageRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private Environment env;
    final Integer MAX = 2;
    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public BillDTO checkBill (Integer packageId, Integer customerId, Integer quantity) {

        Package myPack = EntityCheckHandler.checkEntityExistById(packageRepository, packageId);
        Customer myCustomer = EntityCheckHandler.checkEntityExistById(customerRepository, customerId);

        Instant startDateOfCurrentMonth = getStartDateOfCurrentMonth();
        Instant endDateOfCurrentMonth = getEndDateOfCurrentMonth();
        Customer customer = customerService.getCurrentCredential();
        if (!Objects.equals(customer.getId(), customerId) ||  packageId <= 0 || quantity <= 0)
            throw new BodyBadRequestException(env.getProperty("api.notify.bad_request"));
        // Tính tổng số lần đăng ký hiện tại cho tất cả các gói dịch vụ, bao gồm cả gói ID 1
        Integer totalQuantity = registerPackageRepository
                .sumQuantityByCustomerIdAndDateRange(
                        customerId,startDateOfCurrentMonth, endDateOfCurrentMonth);
        totalQuantity = (totalQuantity == null) ? 0 : totalQuantity;
        // Kiểm tra xem tổng số lần đăng ký đã vượt quá giới hạn (MAX) cho tất cả các gói dịch vụ ngoại trừ gói ID 1
        if (packageId != 1 && totalQuantity + quantity > MAX) {
            throw new BodyBadRequestException("Exceeded maximum registrations");
        } else {
            return createBill(myPack, myCustomer, quantity);
        }
    }

    @Override
    public CustomerPackageRegistration  savePackageRegister
            ( Integer packageId,Integer customerId,Integer quantity){
            CustomerPackageRegistration registration = getCustomerPackageRegistration(
                    packageId, customerId, quantity);
            return registerPackageRepository.save(registration);

    }


    @Override
    public CustomerPackageRegistration getCustomerPackageRegistration(Integer packId,
                                                                       Integer customerId,
                                                                       Integer quantity) {
        Customer customer = EntityCheckHandler.checkEntityExistById(customerRepository, customerId);
        Package pack = EntityCheckHandler.checkEntityExistById(packageRepository, packId);
        CustomerPackageRegistration customerPackageRegistration = new CustomerPackageRegistration();
        customerPackageRegistration.setCustomer(customer);
        customerPackageRegistration.setServicePackage(pack);
        customerPackageRegistration.setQuantity(quantity);
        return customerPackageRegistration;
    }

    private BillDTO createBill(Package pack, Customer customer, Integer quantity) {
        return new BillDTO(
                customer,
                pack,
                quantity,
                Instant.now()
        );
    }

    private Instant getStartDateOfCurrentMonth() {
        YearMonth currentYearMonth = YearMonth.now();
        return currentYearMonth.atDay(1).atStartOfDay().atOffset(ZoneOffset.UTC).toInstant();
    }

    private Instant getEndDateOfCurrentMonth() {
        YearMonth currentYearMonth = YearMonth.now();
        return currentYearMonth.atEndOfMonth().atStartOfDay().atOffset(ZoneOffset.UTC).toInstant();
    }


}
