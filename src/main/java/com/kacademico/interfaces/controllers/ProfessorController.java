package com.kacademico.interfaces.controllers;

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

import com.kacademico.app.dto.professor.ProfessorRequestDTO;
import com.kacademico.app.dto.professor.ProfessorResponseDTO;
import com.kacademico.app.dto.professor.ProfessorUpdateDTO;
import com.kacademico.app.services.ProfessorService;
import com.kacademico.shared.utils.AsyncResultHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/professor")
public class ProfessorController {
    
    private final ProfessorService professorS;


    @Operation(summary = "Create a new professor",
                description = "Create a new professor in the system with the provided parameters. <br>(Wage in cents)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Professor successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid email or wage"),
        @ApiResponse(responseCode = "409", description = "Email already in use"),
        @ApiResponse(responseCode = "422", description = "Name, password or wage is invalid (too short, too long or incorrect format)")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProfessorRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AsyncResultHandler.await(professorS.createAsync(request)));
    }


    
    @Operation(summary = "Get all professors",
                description = "Retrieves a list of all professors in the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professors successfully retrieved")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping    
    public ResponseEntity<List<ProfessorResponseDTO>> readAll() {
        return ResponseEntity.ok(AsyncResultHandler.await(professorS.readAllAsync()));
    }



    @Operation(summary = "Get professor details by ID",
                description = "Retrieves the details of a specific professor identified by the provided ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professor found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Professor not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(AsyncResultHandler.await(professorS.readByIdAsync(id)));
    }    


    
    @Operation(summary = "Update professor by ID",
                description = "Updates the details of a professor identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed. <br>(Wage in cents)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professor successfully updated"),
        @ApiResponse(responseCode = "404", description = "Professor not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid ProfessorUpdateDTO data) {
        return ResponseEntity.ok(AsyncResultHandler.await(professorS.updateAsync(id, data)));
    }



    @Operation(summary = "Delete professor by ID",
                description = "Deletes the professor identified by the provided ID from the system.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Professor successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Professor not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(AsyncResultHandler.await(professorS.deleteAsync(id)));
    }


}