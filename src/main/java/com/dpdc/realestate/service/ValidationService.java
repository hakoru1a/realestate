package com.dpdc.realestate.service;

import com.dpdc.realestate.dto.request.CredentialRegister;

public interface ValidationService {
    void validateCredential(CredentialRegister credentialRegister, boolean isCustomer);
}
