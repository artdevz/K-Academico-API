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

import com.kacademic.dto.student.StudentDetailsDTO;
import com.kacademic.dto.student.StudentRequestDTO;
import com.kacademic.dto.student.StudentResponseDTO;
import com.kacademic.dto.student.StudentUpdateDTO;
import com.kacademic.services.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/student")
@RestController
public class StudentController {
    
    private final StudentService studentS;

    public StudentController(StudentService studentS) {
        this.studentS = studentS;
    }
    
    @Operation(
        summary = "Create a new student",
        description = "Create a new student in the system with the provided parameters."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid email or enrollment format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system."),
        @ApiResponse(responseCode = "409", description = "Enrollment or email already in use"),
        @ApiResponse(responseCode = "422", description = "Enrollment or other fields have invalid length (too short or too long)")
    }) 
    @PostMapping
    public CompletableFuture<ResponseEntity<String>> create(@RequestBody @Valid StudentRequestDTO request) {
        return studentS.createAsync(request).thenApply(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }
    
    @Operation(
        summary = "Get all students",
        description = "Retrieves a list of all students in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Students successfully retrieved")
    })
    @GetMapping    
    public CompletableFuture<ResponseEntity<List<StudentResponseDTO>>> readAll() {
        return studentS.readAllAsync().thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Get student details by ID",
        description = "Retrieves the details of a specific student identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<StudentDetailsDTO>> readById(@PathVariable UUID id) {
        return studentS.readByIdAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }    
    
    @Operation(
        summary = "Update student by ID",
        description = "Updates the details of a student identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student successfully updated"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> update(@PathVariable UUID id, @RequestBody @Valid StudentUpdateDTO data) {
        return studentS.updateAsync(id, data).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Delete student by ID",
        description = "Deletes the student identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> delete(@PathVariable UUID id) {
        return studentS.deleteAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }
    
}
