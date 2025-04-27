package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.equivalence.EquivalenceDetailsDTO;
import com.kacademico.app.dto.equivalence.EquivalenceRequestDTO;
import com.kacademico.app.dto.equivalence.EquivalenceResponseDTO;
import com.kacademico.app.dto.equivalence.EquivalenceUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Equivalence;
import com.kacademico.domain.models.Subject;
import com.kacademico.domain.repositories.EquivalenceRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EquivalenceService {
    
    private final EquivalenceRepository equivalenceR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    public CompletableFuture<String> createAsync(EquivalenceRequestDTO data) {
        Equivalence equivalence = requestMapper.toEquivalence(data);

        equivalenceR.save(equivalence);
        return CompletableFuture.completedFuture("Equivalence successfully Created: " + equivalence.getId());
    }

    @Async
    public CompletableFuture<List<EquivalenceResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(equivalenceR.findAll(), responseMapper::toEquivalenceResponseDTO));
    }

    @Async
    public CompletableFuture<EquivalenceDetailsDTO> readByIdAsync(UUID id) {
        Equivalence equivalence = finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found");
        
        return CompletableFuture.completedFuture(
            new EquivalenceDetailsDTO(
                responseMapper.toEquivalenceResponseDTO(equivalence),
                equivalence.getSubjects().stream().map(Subject::getId).toList()
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, EquivalenceUpdateDTO data) {
        Equivalence equivalence = finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found");
            
        equivalenceR.save(equivalence);
        return CompletableFuture.completedFuture("Updated Equivalence");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found");
        
        equivalenceR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Equivalence");
    }

}
