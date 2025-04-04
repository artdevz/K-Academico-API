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

import com.kacademic.app.dto.enrollee.EnrolleeDetailsDTO;
import com.kacademic.app.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.app.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.app.dto.enrollee.EnrolleeUpdateDTO;
import com.kacademic.app.services.EnrolleeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/enrollee")
public class EnrolleeController {
    
    private final EnrolleeService enrolleeS;


    @Operation(summary = "Create a new enrollee",
                description = "Create a new enrollee in the system with the provided parameters.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Enrollee created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid EnrolleeRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrolleeS.createAsync(request).join());
    }


    
    @Operation(summary = "Get all enrollees",
                description = "Retrieves a list of all enrollees in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Enrollees successfully retrieved")
    })
    @GetMapping    
    public ResponseEntity<List<EnrolleeResponseDTO>> readAll() {
        return ResponseEntity.ok(enrolleeS.readAllAsync().join());
    }



    @Operation(summary = "Get enrollee details by ID",
                description = "Retrieves the details of a specific enrollee identified by the provided ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Enrollee found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Enrollee not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnrolleeDetailsDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(enrolleeS.readByIdAsync(id).join());
    }  
    
    
    
    @Operation(summary = "Update enrollee by ID",
                description = "Updates the details of a enrollee identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Enrollee successfully updated"),
        @ApiResponse(responseCode = "404", description = "Enrollee not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid EnrolleeUpdateDTO data) {
        return ResponseEntity.ok(enrolleeS.updateAsync(id, data).join());
    }



    @Operation(summary = "Delete enrollee by ID",
                description = "Deletes the enrollee identified by the provided ID from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Enrollee successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Enrollee not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(enrolleeS.deleteAsync(id).join());
    }


}