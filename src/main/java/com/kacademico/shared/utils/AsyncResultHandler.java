package com.kacademico.shared.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;

public class AsyncResultHandler {

    public static <T> T await(CompletableFuture<T> future) {
        try {
            return future.join();
        } 
        catch (CompletionException e) {
            Throwable cause = e.getCause();

            // if (cause instanceof AccessDeniedException) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied", cause);
            if (cause instanceof ResponseStatusException rse) throw rse;
            if (cause instanceof AuthenticationException) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Credentials");
            
            // Fallback Log
            System.out.println("Unhadled Async Exception: " + e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected async error", e);
        }
    }
    
}
