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

import com.kacademic.dto.exam.ExamRequestDTO;
import com.kacademic.dto.exam.ExamResponseDTO;
import com.kacademic.dto.exam.ExamUpdateDTO;
import com.kacademic.services.ExamService;

import jakarta.validation.Valid;

@RequestMapping("/exam")
@RestController
public class ExamController {
    
    private final ExamService examS;

    public ExamController(ExamService examS) {
        this.examS = examS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ExamRequestDTO request) {
        return new ResponseEntity<>(examS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<ExamResponseDTO>> readAll() {
        return new ResponseEntity<>(examS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(examS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid ExamUpdateDTO data) {
        return new ResponseEntity<>(examS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(examS.delete(id), HttpStatus.OK);
    }

}
