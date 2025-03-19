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

import com.kacademic.dto.student.StudentDetailsDTO;
import com.kacademic.dto.student.StudentRequestDTO;
import com.kacademic.dto.student.StudentResponseDTO;
import com.kacademic.dto.student.StudentUpdateDTO;
import com.kacademic.services.StudentService;

import jakarta.validation.Valid;

@RequestMapping("/student")
@RestController
public class StudentController {
    
    private final StudentService studentS;

    public StudentController(StudentService studentS) {
        this.studentS = studentS;
    }
    
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid StudentRequestDTO request) {
        return new ResponseEntity<>(studentS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<StudentResponseDTO>> readAll() {
        return new ResponseEntity<>(studentS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailsDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(studentS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid StudentUpdateDTO data) {
        return new ResponseEntity<>(studentS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(studentS.delete(id), HttpStatus.OK);
    }
    
}
