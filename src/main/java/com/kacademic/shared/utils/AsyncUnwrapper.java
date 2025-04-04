package com.kacademic.shared.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AsyncUnwrapper {

    public static <T> T unwrapExceptions(CompletableFuture<T> future) {
        try {
            return future.join();
        } 
        catch (CompletionException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof ResponseStatusException rse) throw rse;
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected async error", ex);
        }
    }
    
}
