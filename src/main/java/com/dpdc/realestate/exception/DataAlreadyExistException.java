package com.dpdc.realestate.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class DataAlreadyExistException extends RuntimeException {
    private final Map<String, String> errors;

    public DataAlreadyExistException(Map<String, String> errors) {
        this.errors = errors;
    }
}