package com.kacademic.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.app.services.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/init")
public class InitController {

    private final UserService userS;
    
    @PostMapping
    public ResponseEntity<String> initAdmin() {
        return ResponseEntity.status(HttpStatus.CREATED).body(userS.createInitialAdmin().join());
    }

}
