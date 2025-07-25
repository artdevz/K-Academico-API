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

import com.kacademico.app.dto.lesson.LessonRequestDTO;
import com.kacademico.app.dto.lesson.LessonResponseDTO;
import com.kacademico.app.dto.lesson.LessonUpdateDTO;
import com.kacademico.app.services.LessonService;
import com.kacademico.shared.utils.AsyncResultHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/lesson")
public class LessonController {
    
    private final LessonService lessonS;


    @Operation(summary = "Create a new lesson",
                description = "Create a new lesson in the system with the provided parameters.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Lesson created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid LessonRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AsyncResultHandler.await(lessonS.createAsync(request)));
    }


    
    @Operation(summary = "Get all lessons",
                description = "Retrieves a list of all lessons in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lessons successfully retrieved")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping    
    public ResponseEntity<List<LessonResponseDTO>> readAll() {
        return ResponseEntity.ok(AsyncResultHandler.await(lessonS.readAllAsync()));
    }



    @Operation(summary = "Get lesson details by ID",
                description = "Retrieves the details of a specific lesson identified by the provided ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lesson found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(AsyncResultHandler.await(lessonS.readByIdAsync(id)));
    }    


    
    @Operation(summary = "Update lesson by ID",
                description = "Updates the details of a lesson identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lesson successfully updated"),
        @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid LessonUpdateDTO data) {
        return ResponseEntity.ok(AsyncResultHandler.await(lessonS.updateAsync(id, data)));
    }



    @Operation(summary = "Delete lesson by ID",
                description = "Deletes the lesson identified by the provided ID from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lesson successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Lesson not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(AsyncResultHandler.await(lessonS.deleteAsync(id)));
    }
    

}