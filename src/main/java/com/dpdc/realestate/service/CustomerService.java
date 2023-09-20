package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Customer;

import java.util.Optional;

public interface CustomerService {

    Customer register(Customer newCustomer) ;

    boolean isExistByUsername(String username, boolean isExcept) ;

    boolean isExistByPhone(String phone, boolean isExcept);

    boolean isExitsByEmail(String email, boolean isExcept);

    Customer findById( Integer id);

    Optional<Customer> findByUsername(String username);

    Customer save(Customer customer);

    Customer updatePassword(Integer id, String newPassword);

    Customer updateStatus (Integer id, Boolean isActive);

    Customer getCurrentCredential();
}
