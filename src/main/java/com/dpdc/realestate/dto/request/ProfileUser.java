package com.dpdc.realestate.dto.request;

import com.dpdc.realestate.models.enumerate.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProfileUser extends CredentialRegister{
    @NotNull(message = "id bắt buột")
    private Integer id;
    private Instant hireDate;
    private String address;
    private Gender gender;

}
