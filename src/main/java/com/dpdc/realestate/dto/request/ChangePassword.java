package com.dpdc.realestate.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePassword {
    // Thêm regex thêm cho phức tạp
    @NotNull(message = "Password can not be null")
    private String currentPassword;
    @NotNull(message = "Re password can not be null")
    private String previousPassword;
}
