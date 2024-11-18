package com.kacademico.controllers;

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

import com.kacademico.dtos.user.UserRequestDTO;
import com.kacademico.dtos.user.UserResponseDTO;
import com.kacademico.services.UserService;

import jakarta.validation.Valid;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userS;

    public UserController(UserService userS) {
        this.userS = userS;
    }
    
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody @Valid UserRequestDTO data) {

        userS.create(data);

        return new ResponseEntity<>("Created User.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<UserResponseDTO>> readAllUsers() {

        return new ResponseEntity<>(userS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable UUID id) {

        UserResponseDTO user = userS.readById(id);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        userS.update(id, fields);
        return new ResponseEntity<>("Updated User", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id) {
        
        userS.delete(id);
        return new ResponseEntity<>("Deleted User", HttpStatus.OK);
           
    }

}
