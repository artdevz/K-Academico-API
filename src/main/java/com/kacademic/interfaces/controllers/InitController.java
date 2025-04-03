package com.kacademic.interfaces.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.app.services.UserService;

@RequestMapping("/init")
@RestController
public class InitController {

    private final UserService userS;

    public InitController(UserService userS) {
        this.userS = userS;
    }
    
    @PostMapping
    public ResponseEntity<String> initAdmin() {
        return new ResponseEntity<>(userS.createInitialAdmin(), HttpStatus.CREATED);
    }

}
