package com.dpdc.realestate.dto.request;

import com.dpdc.realestate.models.enumerate.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProfileUser extends CredentialRegister{
    private Integer id;
    private Instant hireDate;
    private String address;
    private Gender gender;

}
