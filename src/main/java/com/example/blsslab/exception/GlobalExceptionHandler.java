package com.example.blsslab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        final StringBuilder sb = new StringBuilder("Some constraints have been violated:\n");
        e.getConstraintViolations().stream().forEach(
                c -> sb.append("Root bean class: ").append(c.getRootBeanClass()).append("\nField: ").append(c.getPropertyPath()).append("\nMessage: ").append(c.getMessage()).append("\n---\n"));
        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }
}
