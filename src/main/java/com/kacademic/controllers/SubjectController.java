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

import com.kacademic.dto.subject.SubjectRequestDTO;
import com.kacademic.dto.subject.SubjectResponseDTO;
import com.kacademic.dto.subject.SubjectUpdateDTO;
import com.kacademic.services.SubjectService;

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
        return new ResponseEntity<>(subjectS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<SubjectResponseDTO>> readAll() {
        return new ResponseEntity<>(subjectS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(subjectS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid SubjectUpdateDTO data) {
        return new ResponseEntity<>(subjectS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(subjectS.delete(id), HttpStatus.OK);
    }


}
