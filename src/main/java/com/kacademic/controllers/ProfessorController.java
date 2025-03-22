package com.kacademic.controllers;

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

import com.kacademic.dto.professor.ProfessorRequestDTO;
import com.kacademic.dto.professor.ProfessorResponseDTO;
import com.kacademic.dto.professor.ProfessorUpdateDTO;
import com.kacademic.services.ProfessorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/professor")
@RestController
public class ProfessorController {
    
    private final ProfessorService professorS;

    public ProfessorController(ProfessorService professorS) {
        this.professorS = professorS;
    }

    @Operation(
        summary = "Create a new professor",
        description = "Creates a new professor in the system with the provided name, email, password, and wage. <br>(Wage in cents)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Professor successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid email or wage"),
        @ApiResponse(responseCode = "409", description = "Email already in use"),
        @ApiResponse(responseCode = "422", description = "Name, password or wage is invalid (too short, too long or incorrect format)")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProfessorRequestDTO request) {
        return new ResponseEntity<>(professorS.create(request), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get all professors",
        description = "Retrieves a list of all professors in the system."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professors successfully retrieved")
    })
    @GetMapping    
    public ResponseEntity<List<ProfessorResponseDTO>> readAll() {
        return new ResponseEntity<>(professorS.readAll(), HttpStatus.OK);
    }

    @Operation(
        summary = "Get professor details by ID",
        description = "Retrieves the details of a specific professor identified by the provided ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professor found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Professor not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(professorS.readById(id), HttpStatus.OK);
    }    
    
    @Operation(
        summary = "Update professor by ID",
        description = "Updates the details of a professor identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed. <br>(Wage in cents)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professor successfully updated"),
        @ApiResponse(responseCode = "404", description = "Professor not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid ProfessorUpdateDTO data) {
        return new ResponseEntity<>(professorS.update(id, data), HttpStatus.OK);
    }

    @Operation(
        summary = "Delete professor by ID",
        description = "Deletes the professor identified by the provided ID from the system."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professor successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Professor not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(professorS.delete(id), HttpStatus.OK);
    }

}
