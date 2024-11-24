package com.kacademico.controllers;

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

import com.kacademico.dtos.subject.SubjectRequestDTO;
import com.kacademico.dtos.subject.SubjectResponseDTO;
import com.kacademico.services.SubjectService;

import jakarta.validation.Valid;

@RequestMapping("/subject")
@RestController
public class SubjectController {
    
    private final SubjectService subjectS;

    public SubjectController(SubjectService subjectS) {
        this.subjectS = subjectS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid SubjectRequestDTO request) {

        subjectS.create(request);

        return new ResponseEntity<>("Created Subject.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<SubjectResponseDTO>> readAll() {

        return new ResponseEntity<>(subjectS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> readById(@PathVariable UUID id) {

        SubjectResponseDTO response = subjectS.readById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        subjectS.update(id, fields);
        return new ResponseEntity<>("Updated Subject", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        subjectS.delete(id);
        return new ResponseEntity<>("Deleted Subject", HttpStatus.OK);
           
    }


}
