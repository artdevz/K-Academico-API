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

import com.kacademico.dtos.grade.GradeRequestDTO;
import com.kacademico.dtos.grade.GradeResponseDTO;
import com.kacademico.services.GradeService;

import jakarta.validation.Valid;

@RequestMapping("/grade")
@RestController
public class GradeController {
    
    private final GradeService gradeS;

    public GradeController(GradeService gradeS) {
        this.gradeS = gradeS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid GradeRequestDTO request) {

        gradeS.create(request);

        return new ResponseEntity<>("Created Grade.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<GradeResponseDTO>> readAll() {

        return new ResponseEntity<>(gradeS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponseDTO> readById(@PathVariable UUID id) {

        GradeResponseDTO response = gradeS.readById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        gradeS.update(id, fields);
        return new ResponseEntity<>("Updated Grade", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        gradeS.delete(id);
        return new ResponseEntity<>("Deleted Grade", HttpStatus.OK);
           
    }

}
