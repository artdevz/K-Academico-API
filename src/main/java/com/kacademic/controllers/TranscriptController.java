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

import com.kacademic.dto.transcript.TranscriptRequestDTO;
import com.kacademic.dto.transcript.TranscriptResponseDTO;
import com.kacademic.dto.transcript.TranscriptUpdateDTO;
import com.kacademic.services.TranscriptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/transcript")
@RestController
public class TranscriptController {
    
    private final TranscriptService transcriptS;

    public TranscriptController(TranscriptService transcriptS) {
        this.transcriptS = transcriptS;
    }

    @Operation(
        summary = "Create a new transcript",
        description = "Create a new transcript in the system with the provided parameters."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transcript created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid Student ID format provided"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system."),
        @ApiResponse(responseCode = "409", description = "Student is already associated with a Transcript")
    }) 
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid TranscriptRequestDTO request) {
        return new ResponseEntity<>(transcriptS.create(request), HttpStatus.CREATED);
    }
    
    @Operation(
        summary = "Get all transcripts",
        description = "Retrieves a list of all transcripts in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transcripts successfully retrieved")
    })
    @GetMapping    
    public ResponseEntity<List<TranscriptResponseDTO>> readAll() {
        return new ResponseEntity<>(transcriptS.readAll(), HttpStatus.OK);
    }

    @Operation(
        summary = "Get transcript details by ID",
        description = "Retrieves the details of a specific transcript identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transcript found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Transcript not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TranscriptResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(transcriptS.readById(id), HttpStatus.OK);
    }    
    
    @Operation(
        summary = "Update Transcript by ID",
        description = "Updates the details of a transcript identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transcript successfully updated"),
        @ApiResponse(responseCode = "404", description = "Transcript not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid TranscriptUpdateDTO data) {
        return new ResponseEntity<>(transcriptS.update(id, data), HttpStatus.OK);
    }

    @Operation(
        summary = "Delete Transcript by ID",
        description = "Deletes the Transcript identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transcript successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Transcript not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(transcriptS.delete(id), HttpStatus.OK);
    }

}
