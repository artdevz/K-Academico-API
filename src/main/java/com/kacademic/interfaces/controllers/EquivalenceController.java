package com.kacademic.interfaces.controllers;

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

import com.kacademic.app.dto.equivalence.EquivalenceDetailsDTO;
import com.kacademic.app.dto.equivalence.EquivalenceRequestDTO;
import com.kacademic.app.dto.equivalence.EquivalenceResponseDTO;
import com.kacademic.app.dto.equivalence.EquivalenceUpdateDTO;
import com.kacademic.app.services.EquivalenceService;
import com.kacademic.shared.utils.AsyncResultHandler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/equivalence")
public class EquivalenceController {
    
    private final EquivalenceService equivalenceS;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid EquivalenceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AsyncResultHandler.await(equivalenceS.createAsync(request)));
    }



    @GetMapping    
    public ResponseEntity<List<EquivalenceResponseDTO>> readAll() {
        return ResponseEntity.ok(AsyncResultHandler.await(equivalenceS.readAllAsync()));
    }



    @GetMapping("/{id}")
    public ResponseEntity<EquivalenceDetailsDTO> readById(@PathVariable UUID id) {
        return ResponseEntity.ok(AsyncResultHandler.await(equivalenceS.readByIdAsync(id)));
    }  
    
    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid EquivalenceUpdateDTO data) {
        return ResponseEntity.ok(AsyncResultHandler.await(equivalenceS.updateAsync(id, data)));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(AsyncResultHandler.await(equivalenceS.deleteAsync(id)));
    }
    

}
