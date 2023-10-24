package com.dpdc.realestate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class ModelResponse {

    private String message;

    private Object data;


}
