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

import com.kacademic.dto.transcript.TranscriptRequestDTO;
import com.kacademic.dto.transcript.TranscriptResponseDTO;
import com.kacademic.dto.transcript.TranscriptUpdateDTO;
import com.kacademic.services.TranscriptService;

import jakarta.validation.Valid;

@RequestMapping("/transcript")
@RestController
public class TranscriptController {
    
    private final TranscriptService transcriptS;

    public TranscriptController(TranscriptService transcriptS) {
        this.transcriptS = transcriptS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid TranscriptRequestDTO request) {
        return new ResponseEntity<>(transcriptS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<TranscriptResponseDTO>> readAll() {
        return new ResponseEntity<>(transcriptS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranscriptResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(transcriptS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid TranscriptUpdateDTO data) {
        return new ResponseEntity<>(transcriptS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(transcriptS.delete(id), HttpStatus.OK);
    }

}
