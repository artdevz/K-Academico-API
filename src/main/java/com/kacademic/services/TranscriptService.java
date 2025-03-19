package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.transcript.TranscriptRequestDTO;
import com.kacademic.dto.transcript.TranscriptResponseDTO;
import com.kacademic.dto.transcript.TranscriptUpdateDTO;
import com.kacademic.models.Transcript;
import com.kacademic.repositories.TranscriptRepository;

@Service
public class TranscriptService {
    
    private final TranscriptRepository transcriptR;

    private final MappingService mapS;

    private final String entity = "Transcript";

    public TranscriptService(TranscriptRepository transcriptR, MappingService mapS) {
        this.transcriptR = transcriptR;
        this.mapS = mapS;
    }

    public String create(TranscriptRequestDTO data) {

        Transcript transcript = new Transcript(
            mapS.findStudentById(data.student())
        );

        transcriptR.save(transcript);
        return "Created" + entity;
        
    }

    public List<TranscriptResponseDTO> readAll() {

        return transcriptR.findAll().stream()
            .map(transcript -> new TranscriptResponseDTO(
                transcript.getId(),
                transcript.getStudent().getName(),
                transcript.getEnrollees()
            ))
            .collect(Collectors.toList());
    }

    public TranscriptResponseDTO readById(UUID id) {

        Transcript transcript = transcriptR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transcript Not Found."));
        
        return new TranscriptResponseDTO(
            transcript.getId(),
            transcript.getStudent().getName(),
            transcript.getEnrollees()
        );
    }

    public String update(UUID id, TranscriptUpdateDTO data) {

        Transcript transcript = transcriptR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
            
        transcriptR.save(transcript);
        return "Updated" + entity;
        
    }

    public String delete(UUID id) {

        if (!transcriptR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transcript Not Found.");
        
        transcriptR.deleteById(id);
        return "Deleted" + entity;

    }
    
}
