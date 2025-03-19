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

import com.kacademic.dto.evaluation.EvaluationRequestDTO;
import com.kacademic.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.dto.evaluation.EvaluationUpdateDTO;
import com.kacademic.services.EvaluationService;

import jakarta.validation.Valid;

@RequestMapping("/evalution")
@RestController
public class EvalutionController {
    
    private final EvaluationService evalutionS;

    public EvalutionController(EvaluationService evalutionS) {
        this.evalutionS = evalutionS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid EvaluationRequestDTO request) {
        return new ResponseEntity<>(evalutionS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<EvaluationResponseDTO>> readAll() {
        return new ResponseEntity<>(evalutionS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(evalutionS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid EvaluationUpdateDTO data) {
        return new ResponseEntity<>(evalutionS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(evalutionS.delete(id), HttpStatus.OK);
    }

}
