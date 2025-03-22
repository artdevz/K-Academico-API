package com.kacademic.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidArgException(MethodArgumentNotValidException e, WebRequest request) {
        
        FieldError fieldError = e.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
        
        ApiErrorResponse response = new ApiErrorResponse(
            HttpStatus.BAD_REQUEST.toString(),
            fieldError != null ? fieldError.getDefaultMessage() : "Validation Error.",
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
