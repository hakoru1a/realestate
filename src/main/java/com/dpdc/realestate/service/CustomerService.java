package com.dpdc.realestate.service;

import com.dpdc.realestate.models.entity.Customer;

public interface CustomerService {

    Customer register(Customer newCustomer) ;

    boolean isExistByUsername(String username) ;

    boolean isExistByPhone(String phone);

    boolean isExitsByEmail(String email);

}
