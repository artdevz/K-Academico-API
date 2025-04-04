package com.kacademic.shared.exceptions;

import java.time.LocalDateTime;

public record ApiResponse(
    String code,
    String message,
    LocalDateTime timestamp,
    String path
) {}
