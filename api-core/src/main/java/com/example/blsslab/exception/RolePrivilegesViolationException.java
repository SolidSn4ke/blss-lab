package com.example.blsslab.exception;

public class RolePrivilegesViolationException extends RuntimeException {
    public RolePrivilegesViolationException(String msg) {
        super(msg);
    }
}
