package com.kacademic.shared.exceptions;

import java.time.Instant;

public record ApiResponse(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path
) {}
