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

import com.kacademic.app.dto.course.CourseDetailsDTO;
import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.app.dto.course.CourseResponseDTO;
import com.kacademic.app.dto.course.CourseUpdateDTO;
import com.kacademic.app.services.CourseService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {
    
    private final CourseService courseS;


    @Operation(summary = "Create a new course",
                description = "Create a new course in the system with the provided parameters.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CourseRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseS.createAsync(request).join());
    }


    
    @Operation(summary = "Get all courses",
                description = "Retrieves a list of all courses in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Courses successfully retrieved")
    })
    @GetMapping    
    public ResponseEntity<List<CourseResponseDTO>> readAll() {
        return ResponseEntity.ok(courseS.readAllAsync().join());
    }



    @Operation(summary = "Get course details by ID",
                description = "Retrieves the details of a specific course identified by the provided ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Course found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(courseS.readByIdAsync(id).join());
    } 
    
    
    
    @Operation(summary = "Update course by ID",
        description = "Updates the details of a course identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Course successfully updated"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid CourseUpdateDTO data) {
        return ResponseEntity.ok(courseS.updateAsync(id, data).join());
    }



    @Operation(summary = "Delete course by ID",
        description = "Deletes the course identified by the provided ID from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Course successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(courseS.deleteAsync(id).join());
    }


}