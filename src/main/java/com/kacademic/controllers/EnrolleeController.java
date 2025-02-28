package com.kacademic.controllers;

import java.util.List;
import java.util.Map;
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

        enrolleeS.create(request);

        return new ResponseEntity<>("Created Enrollee.", HttpStatus.CREATED);

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
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        enrolleeS.update(id, fields);
        return new ResponseEntity<>("Updated Enrollee", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        enrolleeS.delete(id);
        return new ResponseEntity<>("Deleted Enrollee", HttpStatus.OK);
           
    }

}
