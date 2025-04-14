package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.equivalence.EquivalenceDetailsDTO;
import com.kacademic.app.dto.equivalence.EquivalenceRequestDTO;
import com.kacademic.app.dto.equivalence.EquivalenceResponseDTO;
import com.kacademic.app.dto.equivalence.EquivalenceUpdateDTO;
import com.kacademic.domain.models.Equivalence;
import com.kacademic.domain.models.Subject;
import com.kacademic.domain.repositories.EquivalenceRepository;
import com.kacademic.domain.repositories.SubjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EquivalenceService {
    
    private final EquivalenceRepository equivalenceR;
    private final SubjectRepository subjectR;

    private final AsyncTaskExecutor taskExecutor;

    public CompletableFuture<String> createAsync(EquivalenceRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Equivalence equivalence = new Equivalence(
                data.name(),
                data.subjects().stream().map(this::findSubject).toList()
            );

            equivalenceR.save(equivalence);
            return "Equivalence successfully Created: " + equivalence.getId();
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<EquivalenceResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return (
                equivalenceR.findAll().stream()
                .map(equivalence -> new EquivalenceResponseDTO(
                    equivalence.getId(),
                    equivalence.getName()
                ))
                .collect(Collectors.toList())
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<EquivalenceDetailsDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Equivalence equivalence =findEquivalence(id);
            
            return (
                new EquivalenceDetailsDTO(
                    new EquivalenceResponseDTO(
                        equivalence.getId(),
                        equivalence.getName()
                    ),
                    equivalence.getSubjects().stream().map(Subject::getId).toList()
                )
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, EquivalenceUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Equivalence equivalence = findEquivalence(id);
                
            equivalenceR.save(equivalence);
            return "Updated Equivalence";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            findEquivalence(id);
            
            equivalenceR.deleteById(id);
            return "Deleted Equivalence";
        }, taskExecutor);
    }

    private Equivalence findEquivalence(UUID id) {
        return equivalenceR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equivalence not Found"));
    }

    private Subject findSubject(UUID id) {
        return subjectR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not Found"));
    }

}
