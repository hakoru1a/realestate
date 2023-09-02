package com.dpdc.realestate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelResponse {


    private String message;

    private Object data;

    public ModelResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}
