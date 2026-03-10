package com.example.blsslab.exception;

import org.hibernate.type.descriptor.java.CoercionException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        final StringBuilder sb = new StringBuilder("Some constraints have been violated:\n");
        e.getConstraintViolations().stream().forEach(
                c -> sb.append("Root bean class: ").append(c.getRootBeanClass()).append("\nField: ")
                        .append(c.getPropertyPath()).append("\nMessage: ").append(c.getMessage()).append("\n---\n"));
        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RolePrivilegesViolationException.class)
    public ResponseEntity<String> handleRolePrivilegesViolationException(RolePrivilegesViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestBodyException.class)
    public ResponseEntity<String> handleBadRequestBodyException(BadRequestBodyException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AlreadyProcessedException.class)
    public ResponseEntity<String> handleAlreadyProcessedException(AlreadyProcessedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CoercionException.class)
    public ResponseEntity<String> handle(CoercionException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<String> handle(InvalidDataAccessApiUsageException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
