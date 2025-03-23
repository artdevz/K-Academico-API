package com.kacademic.controllers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

import com.kacademic.dto.exam.ExamRequestDTO;
import com.kacademic.dto.exam.ExamResponseDTO;
import com.kacademic.dto.exam.ExamUpdateDTO;
import com.kacademic.services.ExamService;

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
    public CompletableFuture<ResponseEntity<String>> create(@RequestBody @Valid ExamRequestDTO request) {
        return examS.createAsync(request).thenApply(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }
    
    @Operation(
        summary = "Get all exams",
        description = "Retrieves a list of all exams in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exams successfully retrieved")
    })
    @GetMapping    
    public CompletableFuture<ResponseEntity<List<ExamResponseDTO>>> readAll() {
        return examS.readAllAsync().thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Get exams details by ID",
        description = "Retrieves the details of a specific exams identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exam found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Exam not found")
    })
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ExamResponseDTO>> readById(@PathVariable UUID id) {
        return examS.readByIdAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }    
    
    @Operation(
        summary = "Update exam by ID",
        description = "Updates the details of a exam identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exam successfully updated"),
        @ApiResponse(responseCode = "404", description = "Exam not found")
    })
    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> update(@PathVariable UUID id, @RequestBody @Valid ExamUpdateDTO data) {
        return examS.updateAsync(id, data).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
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
    public CompletableFuture<ResponseEntity<String>> delete(@PathVariable UUID id) {
        return examS.deleteAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

}
