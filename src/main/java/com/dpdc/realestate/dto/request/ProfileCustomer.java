package com.dpdc.realestate.dto.request;

import com.dpdc.realestate.models.enumerate.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProfileCustomer  extends CredentialRegister {

    @NotEmpty(message = "id bắt buột")
    private Integer id;

    private Instant dateOfBirth;

    private Gender gender;

    private String address;

    @NotBlank(message = "occupation không để khoảng trắng")
    private String occupation;


}
