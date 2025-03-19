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

import com.kacademic.dto.enrollee.EnrolleeDetailsDTO;
import com.kacademic.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.dto.enrollee.EnrolleeUpdateDTO;
import com.kacademic.services.EnrolleeService;

import jakarta.validation.Valid;

@RequestMapping("/enrollee")
@RestController
public class EnrolleeController {
    
    private final EnrolleeService enrolleeS;

    public EnrolleeController(EnrolleeService enrolleeS) {
        this.enrolleeS = enrolleeS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid EnrolleeRequestDTO request) {
        return new ResponseEntity<>(enrolleeS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<EnrolleeResponseDTO>> readAll() {
        return new ResponseEntity<>(enrolleeS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrolleeDetailsDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(enrolleeS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid EnrolleeUpdateDTO data) {
        return new ResponseEntity<>(enrolleeS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(enrolleeS.delete(id), HttpStatus.OK);
    }

}
