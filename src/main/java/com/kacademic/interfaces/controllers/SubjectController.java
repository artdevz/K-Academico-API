package com.kacademic.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.app.dto.subject.SubjectRequestDTO;
import com.kacademic.app.dto.subject.SubjectResponseDTO;
import com.kacademic.app.dto.subject.SubjectUpdateDTO;
import com.kacademic.app.services.SubjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/subject")
@RestController
public class SubjectController {
    
    private final SubjectService subjectS;

    public SubjectController(SubjectService subjectS) {
        this.subjectS = subjectS;
    }

    @Operation(
        summary = "Create a new subject",
        description = "Create a new subject in the system with the provided parameters and an Optional List of prerequisites."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Subject created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid semester format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid SubjectRequestDTO request) {
        return new ResponseEntity<>(subjectS.createAsync(request), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get all subjects",
        description = "Retrieves a list of all subjects in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subjects successfully retrieved")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping    
    public ResponseEntity<List<SubjectResponseDTO>> readAll() {
        return new ResponseEntity<>(subjectS.readAllAsync(), HttpStatus.OK);
    }

    @Operation(
        summary = "Get subjects details by ID",
        description = "Retrieves the details of a specific subject identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subject found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(subjectS.readByIdAsync(id), HttpStatus.OK);
    }    
    
    @Operation(
        summary = "Update subject by ID",
        description = "Updates the details of a subject identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subject successfully updated"),
        @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid SubjectUpdateDTO data) {
        return new ResponseEntity<>(subjectS.updateAsync(id, data), HttpStatus.OK);
    }

    @Operation(
        summary = "Delete subject by ID",
        description = "Deletes the subject identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subject successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(subjectS.deleteAsync(id), HttpStatus.OK);
    }
}
