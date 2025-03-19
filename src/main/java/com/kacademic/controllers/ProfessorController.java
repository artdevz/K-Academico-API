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

import com.kacademic.dto.professor.ProfessorRequestDTO;
import com.kacademic.dto.professor.ProfessorResponseDTO;
import com.kacademic.dto.professor.ProfessorUpdateDTO;
import com.kacademic.services.ProfessorService;

import jakarta.validation.Valid;

@RequestMapping("/professor")
@RestController
public class ProfessorController {
    
    private final ProfessorService professorS;

    public ProfessorController(ProfessorService professorS) {
        this.professorS = professorS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid ProfessorRequestDTO request) {
        return new ResponseEntity<>(professorS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<ProfessorResponseDTO>> readAll() {
        return new ResponseEntity<>(professorS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(professorS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid ProfessorUpdateDTO data) {
        return new ResponseEntity<>(professorS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(professorS.delete(id), HttpStatus.OK);
    }

}
