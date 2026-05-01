package com.example.blsslab.exception;

public class BadRequestBodyException extends RuntimeException {
    public BadRequestBodyException(String msg) {
        super(msg);
    }
}
