package com.kacademico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    private String extractError(String message) {
        message = message.substring(message.indexOf("='") + 2, message.indexOf(".',"));
        System.out.println(message);
        return message;

    }

}
