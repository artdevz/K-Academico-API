package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.transcript.TranscriptRequestDTO;
import com.kacademic.dto.transcript.TranscriptResponseDTO;
import com.kacademic.models.Transcript;
import com.kacademic.repositories.TranscriptRepository;

@Service
public class TranscriptService {
    
    private final TranscriptRepository transcriptR;

    private final MappingService mapS;

    public TranscriptService(TranscriptRepository transcriptR, MappingService mapS) {
        this.transcriptR = transcriptR;
        this.mapS = mapS;
    }

    public void create(TranscriptRequestDTO data) {

        Transcript transcript = new Transcript(
            mapS.findStudentById(data.student())
        );

        transcriptR.save(transcript);
        
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Boletim não encontrado."));
        
        return new TranscriptResponseDTO(
            transcript.getId(),
            transcript.getStudent().getName(),
            transcript.getEnrollees()
        );
    }

    public Transcript update(UUID id, Map<String, Object> fields) {

        Optional<Transcript> existingTranscript = transcriptR.findById(id);
    
        if (existingTranscript.isPresent()) {
            Transcript transcript = existingTranscript.get();
    
            fields.forEach((key, value) -> {
                switch (key) {                

                    default:
                        Field field = ReflectionUtils.findField(Transcript.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, transcript, value);
                        }
                        break;
                }
            });
            
            return transcriptR.save(transcript);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Boletim não encontrado.");
        
    }

    public void delete(UUID id) {

        if (!transcriptR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Boletim não encontrado.");
        transcriptR.deleteById(id);

    }
    
}
