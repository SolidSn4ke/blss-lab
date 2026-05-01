package com.example.blsslab.exception;

public class AlreadyProcessedException extends RuntimeException {
    public AlreadyProcessedException(String msg) {
        super(msg);
    }
}
