package com.dpdc.realestate.dto.request;

import com.dpdc.realestate.models.enumerate.Gender;
import lombok.Data;

import java.time.Instant;

@Data
public class ProfileCustomer {

    private Instant dateOfBirth;

    private Gender gender;

    private String address;

    private String avatar;

    private String occupation;

}
