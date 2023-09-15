package com.dpdc.realestate.exception;

import java.util.Map;

public class DataAlreadyExistException extends RuntimeException {
    private final Map<String, String> errors;

    public DataAlreadyExistException(Map<String, String> errors) {
        this.errors = errors;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}