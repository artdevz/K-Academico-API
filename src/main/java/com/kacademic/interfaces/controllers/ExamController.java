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

import com.kacademic.app.dto.exam.ExamRequestDTO;
import com.kacademic.app.dto.exam.ExamResponseDTO;
import com.kacademic.app.dto.exam.ExamUpdateDTO;
import com.kacademic.app.services.ExamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/exam")
@RestController
public class ExamController {
    
    private final ExamService examS;

    public ExamController(ExamService examS) {
        this.examS = examS;
    }

    @Operation(
        summary = "Create a new exam",
        description = "Create a new exam in the system with the provided parameters."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Exam created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ExamRequestDTO request) {
        return new ResponseEntity<>(examS.createAsync(request), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get all exams",
        description = "Retrieves a list of all exams in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exams successfully retrieved")
    })
    @GetMapping    
    public ResponseEntity<List<ExamResponseDTO>> readAll() {
        return new ResponseEntity<>(examS.readAllAsync(), HttpStatus.OK);
    }

    @Operation(
        summary = "Get exam details by ID",
        description = "Retrieves the details of a specific exam identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exam found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Exam not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(examS.readByIdAsync(id), HttpStatus.OK);
    }    

    @Operation(
        summary = "Update exam by ID",
        description = "Updates the details of an exam identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exam successfully updated"),
        @ApiResponse(responseCode = "404", description = "Exam not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid ExamUpdateDTO data) {
        return new ResponseEntity<>(examS.updateAsync(id, data), HttpStatus.OK);
    }

    @Operation(
        summary = "Delete exam by ID",
        description = "Deletes the exam identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exam successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Exam not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(examS.deleteAsync(id), HttpStatus.OK);
    }

}