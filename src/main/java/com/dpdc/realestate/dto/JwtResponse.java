package com.dpdc.realestate.dto;

import lombok.Getter;

import java.io.Serializable;
import java.util.Date;


@Getter
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;

    private final String accessToken;


    private final String refreshToken;

    private final String expirationDate;

    public JwtResponse(String accessToken,String refreshToken, String expirationDate) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expirationDate = expirationDate;
    }
}