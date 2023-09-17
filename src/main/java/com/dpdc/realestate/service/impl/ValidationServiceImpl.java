package com.dpdc.realestate.service.impl;

import com.dpdc.realestate.dto.request.CredentialRegister;
import com.dpdc.realestate.dto.request.ProfileCustomer;
import com.dpdc.realestate.exception.DataAlreadyExistException;
import com.dpdc.realestate.models.entity.User;
import com.dpdc.realestate.service.CustomerService;
import com.dpdc.realestate.service.UserService;
import com.dpdc.realestate.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private Environment env;

    @Override
    public void validateCredential(CredentialRegister credential, boolean isCustomer) {
        Map<String, String> errors = new HashMap<>();

        if (customerService.isExistByPhone(credential.getPhone(), isCustomer) ||
                userService.isExistByPhone(credential.getPhone(), !isCustomer)) {
            errors.put("phone", env.getProperty("api.notify.already_exist"));
        }
        if (customerService.isExistByUsername(credential.getUsername(), isCustomer) ||
                userService.isExistByUsername(credential.getUsername(), !isCustomer)) {
            errors.put("username", env.getProperty("api.notify.already_exist"));
        }
        if (customerService.isExitsByEmail(credential.getEmail(), isCustomer) ||
                userService.isExitsByEmail(credential.getEmail(), !isCustomer)) {
            errors.put("email", env.getProperty("api.notify.already_exist"));
        }

        if (!errors.isEmpty()) {
            throw new DataAlreadyExistException(errors);
        }
    }

}
