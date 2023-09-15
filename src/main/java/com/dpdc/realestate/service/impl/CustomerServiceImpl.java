package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.models.entity.Customer;
import com.dpdc.realestate.repository.CustomerRepository;
import com.dpdc.realestate.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Customer register(Customer newCustomer)  {
        newCustomer.setPassword(passwordEncoder.encode(newCustomer.getPassword()));
        return customerRepository.save(newCustomer);
    }

    @Override
    public boolean isExistByUsername(String username) {
        return customerRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean isExistByPhone(String phone) {
        return customerRepository.findByPhone(phone).isPresent();
    }

    @Override
    public boolean isExitsByEmail(String email) {
        return customerRepository.findByEmail(email).isPresent();
    }

}
