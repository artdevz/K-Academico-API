package com.kacademico.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademico.app.services.UserService;
import com.kacademico.shared.utils.AsyncResultHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/init")
public class InitController {

    private final UserService userS;
    
    @PostMapping
    public ResponseEntity<String> initAdmin() {
        return ResponseEntity.status(HttpStatus.CREATED).body(AsyncResultHandler.await(userS.createInitialAdmin()));
    }

}
