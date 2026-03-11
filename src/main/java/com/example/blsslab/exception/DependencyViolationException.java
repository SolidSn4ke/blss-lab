package com.example.blsslab.exception;

public class DependencyViolationException extends RuntimeException {
    public DependencyViolationException(String msg) {
        super(msg);
    }
}
