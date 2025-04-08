package com.kacademic.interfaces.controllers;

import java.util.List;
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

import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.dto.user.UserResponseDTO;
import com.kacademic.app.dto.user.UserUpdateDTO;
import com.kacademic.app.services.UserService;
import com.kacademic.shared.utils.AsyncUnwrapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userS;

    
    @Operation(summary = "Create a new user",
                description = "Create a new user in the system with the provided parameters.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid email"),
        @ApiResponse(responseCode = "409", description = "Email already in use"),
        @ApiResponse(responseCode = "422", description = "Name or password length is invalid (too short or too long)")
    })    
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid UserRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AsyncUnwrapper.await(userS.createAsync(request)));
    }


    
    @Operation(summary = "Get all users",
                description = "Retrieves a list of all users in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Users successfully retrieved")
    })    
    @GetMapping    
    public ResponseEntity<List<UserResponseDTO>> readAll() {
        return ResponseEntity.ok(userS.readAllAsync().join());
    }



    @Operation(summary = "Get user details by ID",
                description = "Retrieves the details of a specific user identified by the provided ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(userS.readByIdAsync(id).join());
    }    


    
    @Operation(summary = "Update user by ID",
                description = "Updates the details of a user identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User successfully updated"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid UserUpdateDTO data) {
        return ResponseEntity.ok(userS.updateAsync(id, data).join());
    }



    @Operation(summary = "Delete user by ID",
                description = "Deletes the user identified by the provided ID from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User successfully deleted"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(userS.deleteAsync(id).join());
    }



}