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

import com.kacademic.app.dto.evaluation.EvaluationRequestDTO;
import com.kacademic.app.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.app.dto.evaluation.EvaluationUpdateDTO;
import com.kacademic.app.services.EvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {
    
    private final EvaluationService evaluationS;


    @Operation(summary = "Create a new evaluation",
                description = "Create a new evaluation in the system with the provided parameters.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evaluation created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid EvaluationRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluationS.createAsync(request));
    }


    
    @Operation(summary = "Get all evaluations",
        description = "Retrieves a list of all evaluations in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluations successfully retrieved")
    })
    @GetMapping    
    public ResponseEntity<List<EvaluationResponseDTO>> readAll() {
        return ResponseEntity.ok(evaluationS.readAllAsync());
    }



    @Operation(summary = "Get evaluation details by ID",
                description = "Retrieves the details of a specific evaluation identified by the provided ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(evaluationS.readByIdAsync(id));
    }
    
    

    @Operation(summary = "Update evaluation by ID",
                description = "Updates the details of a evaluation identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation successfully updated"),
        @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid EvaluationUpdateDTO data) {
        return ResponseEntity.ok(evaluationS.updateAsync(id, data));
    }



    @Operation(summary = "Delete evaluation by ID",
                description = "Deletes the evaluation identified by the provided ID from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(evaluationS.deleteAsync(id));
    }


}