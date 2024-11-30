package com.kacademico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

@RestControllerAdvice
public class GlobalHandlerException {
    
    @Getter
    public static class ApiErrorResponse {

        private String message;
        private String code;

        public ApiErrorResponse(String message, String code) {
            this.message = message;
            this.code = code;
        }

    }

    @ExceptionHandler(DuplicateValueException.class)
    @ResponseBody
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityException(DuplicateValueException e) {
        ApiErrorResponse response = new ApiErrorResponse(e.getMessage(), "DUPLICATE_DATA");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException e) {
        ApiErrorResponse response = new ApiErrorResponse(extractError(e.getMessage()), "CONSTRAINT_VIOLATION");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(LengthException.class)
    public ResponseEntity<ApiErrorResponse> handleLengthException(LengthException e) {
        ApiErrorResponse response = new ApiErrorResponse(e.getMessage(), "LENGTH_VIOLATION");
        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidArgException(MethodArgumentNotValidException e) {
        
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) errorMessage.append(fieldError.getDefaultMessage());
        
        ApiErrorResponse response = new ApiErrorResponse(errorMessage.toString(), "REGEX_VIOLATION");
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatusException(ResponseStatusException e) {
        ApiErrorResponse response = new ApiErrorResponse(e.getReason(), "NOT_FOUND");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    private String extractError(String message) {
        message = message.substring(message.indexOf("='") + 2, message.indexOf(".',"));
        System.out.println(message);
        return message;

    }

}
