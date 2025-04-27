package com.kacademico.app.helpers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class EntityFinder {
    
    public <T> T findByIdOrThrow(Optional<T> optional, String notFoundMessage) {
        return optional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, notFoundMessage));
    }

}
