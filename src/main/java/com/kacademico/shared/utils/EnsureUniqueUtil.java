package com.kacademico.shared.utils;

import java.util.function.Supplier;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EnsureUniqueUtil {
    
    public static <T> void ensureUnique(Supplier<Optional<T>> finder, Supplier<String> errorMessageSuplier) {
        if (finder.get().isPresent()) throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessageSuplier.get());
    }

}
