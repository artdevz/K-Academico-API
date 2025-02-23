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

import com.kacademic.dto.exam.ExamRequestDTO;
import com.kacademic.dto.exam.ExamResponseDTO;
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

        examS.create(request);

        return new ResponseEntity<>("Created Exam.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<ExamResponseDTO>> readAll() {

        return new ResponseEntity<>(examS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> readById(@PathVariable UUID id) {

        ExamResponseDTO response = examS.readById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        examS.update(id, fields);
        
        return new ResponseEntity<>("Updated Exam", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        examS.delete(id);

        return new ResponseEntity<>("Deleted Exam", HttpStatus.OK);
           
    }

}
