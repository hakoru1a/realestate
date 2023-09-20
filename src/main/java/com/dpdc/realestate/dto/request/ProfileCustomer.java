package com.dpdc.realestate.dto.request;

import com.dpdc.realestate.models.enumerate.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProfileCustomer  extends CredentialRegister {

    @NotNull(message = "id bắt buột")
    private Integer id;

    private Instant dateOfBirth;

    private Gender gender;

    private String address;

    private String occupation;


}
