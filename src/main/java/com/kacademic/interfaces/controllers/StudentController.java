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

import com.kacademic.app.dto.student.StudentDetailsDTO;
import com.kacademic.app.dto.student.StudentRequestDTO;
import com.kacademic.app.dto.student.StudentResponseDTO;
import com.kacademic.app.dto.student.StudentUpdateDTO;
import com.kacademic.app.services.StudentService;
import com.kacademic.shared.utils.AsyncResultHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class StudentController {
    
    private final StudentService studentS;

    
    @Operation(summary = "Create a new student",
                description = "Create a new student in the system with the provided parameters.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Student created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid email or enrollment format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system."),
        @ApiResponse(responseCode = "409", description = "Enrollment or email already in use"),
        @ApiResponse(responseCode = "422", description = "Enrollment or other fields have invalid length (too short or too long)")
    }) 
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid StudentRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AsyncResultHandler.await(studentS.createAsync(request)));
    }


    
    @Operation(summary = "Get all students",
                description = "Retrieves a list of all students in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Students successfully retrieved")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping    
    public ResponseEntity<List<StudentResponseDTO>> readAll() {
        return ResponseEntity.ok(studentS.readAllAsync().join());
    }



    @Operation(summary = "Get student details by ID",
                description = "Retrieves the details of a specific student identified by the provided ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(studentS.readByIdAsync(id).join());
    }    


    
    @Operation(summary = "Update student by ID",
                description = "Updates the details of a student identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student successfully updated"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid StudentUpdateDTO data) {
        return ResponseEntity.ok(studentS.updateAsync(id, data).join());
    }



    @Operation(summary = "Delete student by ID",
                description = "Deletes the student identified by the provided ID from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Student successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(studentS.deleteAsync(id).join());
    }


}