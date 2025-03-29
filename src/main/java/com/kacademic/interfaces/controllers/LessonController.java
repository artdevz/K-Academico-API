package com.kacademic.interfaces.controllers;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

import com.kacademic.app.dto.lesson.LessonRequestDTO;
import com.kacademic.app.dto.lesson.LessonResponseDTO;
import com.kacademic.app.dto.lesson.LessonUpdateDTO;
import com.kacademic.app.services.LessonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/lesson")
@RestController
public class LessonController {
    
    private final LessonService lessonS;

    public LessonController(LessonService lessonS) {
        this.lessonS = lessonS;
    }

    @Operation(
        summary = "Create a new lesson",
        description = "Create a new lesson in the system with the provided parameters."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Lesson created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public CompletableFuture<ResponseEntity<String>> create(@RequestBody @Valid LessonRequestDTO request) {
        return lessonS.createAsync(request).thenApply(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }
    
    @Operation(
        summary = "Get all lessons",
        description = "Retrieves a list of all lessons in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lessons successfully retrieved")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping    
    public CompletableFuture<ResponseEntity<List<LessonResponseDTO>>> readAll() {
        return lessonS.readAllAsync().thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Get lessons details by ID",
        description = "Retrieves the details of a specific lessons identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lesson found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<LessonResponseDTO>> readById(@PathVariable UUID id) {
        return lessonS.readByIdAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }    
    
    @Operation(
        summary = "Update lesson by ID",
        description = "Updates the details of a lesson identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lesson successfully updated"),
        @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> update(@PathVariable UUID id, @RequestBody @Valid LessonUpdateDTO data) {
        return lessonS.updateAsync(id, data).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Delete lesson by ID",
        description = "Deletes the lesson identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lesson successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> delete(@PathVariable UUID id) {
        return lessonS.deleteAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

}
