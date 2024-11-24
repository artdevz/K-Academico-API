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

import com.kacademico.dtos.professor.ProfessorRequestDTO;
import com.kacademico.dtos.professor.ProfessorResponseDTO;
import com.kacademico.services.ProfessorService;

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

        professorS.create(request);

        return new ResponseEntity<>("Created Professor.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<ProfessorResponseDTO>> readAll() {

        return new ResponseEntity<>(professorS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> readById(@PathVariable UUID id) {

        ProfessorResponseDTO response = professorS.readById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        professorS.update(id, fields);
        
        return new ResponseEntity<>("Updated Professor", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        professorS.delete(id);

        return new ResponseEntity<>("Deleted Professor", HttpStatus.OK);
           
    }

}
