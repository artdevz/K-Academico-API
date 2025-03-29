package com.kacademic.shared.exceptions;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> handleAuthenticationException(AuthenticationException e, WebRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.UNAUTHORIZED.toString(),
            "Unathorized: Invalid credentials",
            LocalDateTime.now(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidArgException(MethodArgumentNotValidException e, WebRequest request) {
        
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        HttpStatus status = fieldErrors.isEmpty() ? HttpStatus.BAD_REQUEST : HttpStatus.UNPROCESSABLE_ENTITY;

        ApiErrorResponse response = new ApiErrorResponse(
            status.toString(),
            fieldErrors.isEmpty() ? "Validation Error." : fieldErrors.get(0).getDefaultMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.toString(),
            "Invalid Request: incorrect data format sent.",
            LocalDateTime.now(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(ResponseStatusException e, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
            e.getStatusCode().toString(),
            e.getReason(),
            LocalDateTime.now(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, e.getStatusCode());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception e, WebRequest request) {

        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.toString(),
            "Internal Server Error.",
            LocalDateTime.now(),
            request.getDescription(false)
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
