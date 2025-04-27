package com.kacademico.shared.exceptions;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        ApiResponse response = new ApiResponse(
            Instant.now(), 
            HttpStatus.FORBIDDEN.value(), 
            HttpStatus.FORBIDDEN.name(), 
            "Access Denied :(", 
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException e, WebRequest request) {
        ApiResponse response = new ApiResponse(
            Instant.now(),
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.name(),
            "Unathorized: Invalid Credentials",
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleInvalidArgException(MethodArgumentNotValidException e, WebRequest request) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        HttpStatus status = fieldErrors.isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.UNPROCESSABLE_ENTITY;

        ApiResponse response = new ApiResponse(
            Instant.now(),
            status.value(),
            status.toString().substring(4),
            fieldErrors.isEmpty() ? "Validation Error." : fieldErrors.get(0).getDefaultMessage(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse> handleMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        ApiResponse response = new ApiResponse(
            Instant.now(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.name(),
            "Invalid Request: incorrect data format sent",
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleResponseStatusException(ResponseStatusException e, WebRequest request) {
        ApiResponse response = new ApiResponse(
            Instant.now(),
            e.getStatusCode().value(),
            e.getStatusCode().toString().substring(4),
            e.getReason(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, e.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception e, WebRequest request) {
        ApiResponse response = new ApiResponse(
            Instant.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.name(),
            "Internal Server Error",
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}