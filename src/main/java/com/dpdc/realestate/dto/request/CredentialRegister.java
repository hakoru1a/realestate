package com.dpdc.realestate.dto.request;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CredentialRegister {
    @NotEmpty(message = "username bắt buộc")
    private String username;

//    @NotEmpty(message = "password bắt buộc")
    private String password;
    @NotEmpty(message = "email bắt buộc")
    @Email(message = "email không hợp lệ")
    private String email;
    @NotEmpty(message = "phonenumber bắt buộc")
    @Size(min = 10, max = 11, message = "số điện thoại sai")
    private String phone;
    @NotEmpty(message = "fullname bắt buộc")
    @Size(max = 100, message = "tên quá dài")
    private String fullname;


    private String about;
}
