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

import com.kacademic.dto.attendance.AttendanceRequestDTO;
import com.kacademic.dto.attendance.AttendanceResponseDTO;
import com.kacademic.dto.attendance.AttendanceUpdateDTO;
import com.kacademic.services.AttendanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/attendance")
@RestController
public class AttendanceController {
    
    private final AttendanceService attendanceS;

    public AttendanceController(AttendanceService attendanceS) {
        this.attendanceS = attendanceS;
    }

    @Operation(
        summary = "Create a new attendance",
        description = "Create a new attendance in the system with the provided parameters."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Attendance created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data format"),
        @ApiResponse(responseCode = "404", description = "Resource not found. The provided ID(s) do not match any existing record(s) in the system.")
    })
    @PostMapping
    public CompletableFuture<ResponseEntity<String>> create(@RequestBody @Valid AttendanceRequestDTO request) {
        return attendanceS.createAsync(request).thenApply(response -> new ResponseEntity<>(response, HttpStatus.CREATED));
    }
    
    @Operation(
        summary = "Get all attendances",
        description = "Retrieves a list of all attendances in the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Attendances successfully retrieved")
    })
    @GetMapping    
    public CompletableFuture<ResponseEntity<List<AttendanceResponseDTO>>> readAll() {
        return attendanceS.readAllAsync().thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Get attendance details by ID",
        description = "Retrieves the details of a specific attendance identified by the provided ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Attendance found and retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<AttendanceResponseDTO>> readById(@PathVariable UUID id) {
        return attendanceS.readByIdAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }    
    
    @Operation(
        summary = "Update attendance by ID",
        description = "Updates the details of a attendance identified by the provided ID. Only the specified fields will be updated. <br>If any field is passed as null in the request, it will not be changed"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Attendance successfully updated"),
        @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @PatchMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> update(@PathVariable UUID id, @RequestBody @Valid AttendanceUpdateDTO data) {
        return attendanceS.updateAsync(id, data).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Operation(
        summary = "Delete attendance by ID",
        description = "Deletes the attendance identified by the provided ID from the system"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Attendance successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @DeleteMapping("/{id}")
    public CompletableFuture<ResponseEntity<String>> delete(@PathVariable UUID id) {
        return attendanceS.deleteAsync(id).thenApply(response -> new ResponseEntity<>(response, HttpStatus.OK));
    }

}
