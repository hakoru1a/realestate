package com.dpdc.realestate.dto.response;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data

public class TestValidator {
    @NotEmpty(message = "Thiếu username")
    private String username;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Thiếu password")
    @Min(value = 8, message = "Password phải từ 8 kí tự trở lên")
    private String password;
}
