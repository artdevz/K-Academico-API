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

import com.kacademic.app.dto.grade.GradeRequestDTO;
import com.kacademic.app.dto.grade.GradeResponseDTO;
import com.kacademic.app.dto.grade.GradeUpdateDTO;
import com.kacademic.app.services.GradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/grade")
@RestController
public class GradeController {
    
    private final GradeService gradeS;

    public GradeController(GradeService gradeS) {
        this.gradeS = gradeS;
    }

    @Operation(
        summary = "Create a new grade",
        description = "Create a new grade in the system with the provided parameters. <br>Note: Timetable it's in HH:mm."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Grade created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid GradeRequestDTO request) {
        return new ResponseEntity<>(gradeS.createAsync(request), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get all grades",
        description = "Retrieves a list of all grades in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grades successfully retrieved")
    })
    @GetMapping    
    public ResponseEntity<List<GradeResponseDTO>> readAll() {
        return new ResponseEntity<>(gradeS.readAllAsync(), HttpStatus.OK);
    }

    @Operation(
        summary = "Get grade details by ID",
        description = "Retrieves the details of a specific grade identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grade found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<GradeResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(gradeS.readByIdAsync(id), HttpStatus.OK);
    }    

    @Operation(
        summary = "Update grade by ID",
        description = "Updates the details of a grade identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grade successfully updated"),
        @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid GradeUpdateDTO data) {
        return new ResponseEntity<>(gradeS.updateAsync(id, data), HttpStatus.OK);
    }

    @Operation(
        summary = "Delete grade by ID",
        description = "Deletes the grade identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grade successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(gradeS.deleteAsync(id), HttpStatus.OK);
    }

}