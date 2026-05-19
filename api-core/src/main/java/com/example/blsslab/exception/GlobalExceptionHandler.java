package com.example.blsslab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("Constraint violation: {}", e.getMessage());

        final StringBuilder sb = new StringBuilder("Some constraints have been violated:\n");
        e.getConstraintViolations().stream().forEach(
                c -> sb.append("Root bean class: ").append(c.getRootBeanClass())
                        .append("\nField: ").append(c.getPropertyPath())
                        .append("\nMessage: ").append(c.getMessage())
                        .append("\n---\n"));

        return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RolePrivilegesViolationException.class)
    public ResponseEntity<String> handleRolePrivilegesViolationException(RolePrivilegesViolationException e) {
        log.warn("Access denied: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestBodyException.class)
    public ResponseEntity<String> handleBadRequestBodyException(BadRequestBodyException e) {
        log.warn("Bad request: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyProcessedException.class)
    public ResponseEntity<String> handleAlreadyProcessedException(AlreadyProcessedException e) {
        log.warn("Already processed: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<String> handleOptimisticLockException(OptimisticLockException e) {
        log.warn("Optimistic lock conflict: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredjwtException(ExpiredJwtException e) {
        log.warn("JWT token expired: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException e) {
        log.error("Unhandled exception: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
