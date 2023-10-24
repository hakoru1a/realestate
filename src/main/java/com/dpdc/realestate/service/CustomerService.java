package com.dpdc.realestate.service;

import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.models.entity.Property;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CustomerService {

    Customer register(Customer newCustomer) ;

    List<Customer> getAllCustomer();
    Customer update(Customer update) ;

    boolean isExistByUsername(String username, boolean isExcept) ;

    boolean isExistByPhone(String phone, boolean isExcept);

    boolean isExitsByEmail(String email, boolean isExcept);


    boolean isExistByUsername(Integer customerId, String username, boolean isExcept) ;

    boolean isExistByPhone(Integer customerId,String phone, boolean isExcept);

    boolean isExitsByEmail(Integer customerId,String email, boolean isExcept);


    Customer findById( Integer id);

    Optional<Customer> findByUsername(String username);

    Customer save(Customer customer);

    Customer updatePassword(Integer id, String newPassword);

    Customer updateStatus (Integer id, Boolean isActive);

    Customer getCurrentCredential();

    void activeByUsername(String username);

    Customer setTurn(Integer customerId, Integer turn, boolean isMinus);

    void deleteById(Integer id);
}
