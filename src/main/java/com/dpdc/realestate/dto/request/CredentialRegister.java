package com.dpdc.realestate.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CredentialRegister {
    @NotEmpty(message = "Thiếu username")
    private String username;
    @NotEmpty(message = "Thiếu password")
    private String password;
}
