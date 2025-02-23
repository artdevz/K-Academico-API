package com.kacademic.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.dto.user.UserResponseDTO;
import com.kacademic.services.UserService;

import jakarta.validation.Valid;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userS;

    public UserController(UserService userS) {
        this.userS = userS;
    }
    
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid UserRequestDTO request) {

        userS.create(request);

        return new ResponseEntity<>("Created User.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<UserResponseDTO>> readAll() {

        return new ResponseEntity<>(userS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> readById(@PathVariable UUID id) {

        return new ResponseEntity<>(userS.readById(id), HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        userS.update(id, fields);
        return new ResponseEntity<>("Updated User", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        userS.delete(id);
        return new ResponseEntity<>("Deleted User", HttpStatus.OK);
           
    }

}
