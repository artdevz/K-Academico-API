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

import com.kacademic.dto.evaluation.EvaluationRequestDTO;
import com.kacademic.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.dto.evaluation.EvaluationUpdateDTO;
import com.kacademic.services.EvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/evalution")
@RestController
public class EvalutionController {
    
    private final EvaluationService evalutionS;

    public EvalutionController(EvaluationService evalutionS) {
        this.evalutionS = evalutionS;
    }

    @Operation(
        summary = "Create a new evaluation",
        description = "Create a new evaluation in the system with the provided parameters."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evaluation created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public CompletableFuture<ResponseEntity<String>> create(@RequestBody @Valid EvaluationRequestDTO request) {
        return evalutionS.createAsync(request).thenApply(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }
    
    @Operation(
        summary = "Get all evaluations",
        description = "Retrieves a list of all evaluations in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluations successfully retrieved")
    })
    @GetMapping    
    public CompletableFuture<ResponseEntity<List<EvaluationResponseDTO>>> readAll() {
        return evalutionS.readAllAsync().thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Get evaluation details by ID",
        description = "Retrieves the details of a specific evaluation identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<EvaluationResponseDTO>> readById(@PathVariable UUID id) {
        return evalutionS.readByIdAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }    

    @Operation(
        summary = "Update evaluation by ID",
        description = "Updates the details of a evaluation identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation successfully updated"),
        @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> update(@PathVariable UUID id, @RequestBody @Valid EvaluationUpdateDTO data) {
        return evalutionS.updateAsync(id, data).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Delete evaluation by ID",
        description = "Deletes the evaluation identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Evaluation not found")
    })
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> delete(@PathVariable UUID id) {
        return evalutionS.deleteAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

}
