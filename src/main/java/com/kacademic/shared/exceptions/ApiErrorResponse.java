package com.kacademic.shared.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiErrorResponse {
    
    private final String code;
    private final String message;
    private final LocalDateTime timestamp;
    private final String path;

}
