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

import com.kacademico.dtos.evaluation.EvaluationRequestDTO;
import com.kacademico.dtos.evaluation.EvaluationResponseDTO;
import com.kacademico.services.EvaluationService;

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

        evalutionS.create(request);

        return new ResponseEntity<>("Created Evalution.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<EvaluationResponseDTO>> readAll() {

        return new ResponseEntity<>(evalutionS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDTO> readById(@PathVariable UUID id) {

        EvaluationResponseDTO response = evalutionS.readById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        evalutionS.update(id, fields);
        return new ResponseEntity<>("Updated Evalution", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        evalutionS.delete(id);
        return new ResponseEntity<>("Deleted Evalution", HttpStatus.OK);
           
    }

}
