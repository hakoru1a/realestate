package com.dpdc.realestate.dto.request;

import lombok.Data;

@Data
public class Mail {
    private String content;
    private String subject;
    private String toMail;
}
