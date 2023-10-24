package com.dpdc.realestate.exception;

public class AccountActiveException extends RuntimeException {
    public AccountActiveException(String message) {
        super(message);
    }
}