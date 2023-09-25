package com.dpdc.realestate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Mail {
    private String content;
    private String subject;
    private String toMail;
}
