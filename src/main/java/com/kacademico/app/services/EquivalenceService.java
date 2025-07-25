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
import com.kacademico.domain.repositories.IEquivalenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EquivalenceService {
    
    private final IEquivalenceRepository equivalenceR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    public CompletableFuture<String> createAsync(EquivalenceRequestDTO data) {
        log.info("[API] Iniciando criação de equivalência com nome: {}", data.name());
        
        Equivalence equivalence = requestMapper.toEquivalence(data);
        Equivalence saved = equivalenceR.save(equivalence);

        log.info("[API] Equivalência criada com sucesso. ID: {}", saved.getId());
        return CompletableFuture.completedFuture("Equivalence successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<EquivalenceResponseDTO>> readAllAsync() {
        log.debug("[API] Buscando todas as equivalências");
        List<EquivalenceResponseDTO> response = responseMapper.toResponseDTOList(equivalenceR.findAll(), responseMapper::toEquivalenceResponseDTO);

        log.debug("[API] Encontradas {} equivalências", response.size());
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<EquivalenceDetailsDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscando equivalência com ID: {}", id);
        Equivalence equivalence = finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found");
        
        log.debug("[API] Equivalência encontrada: {}", equivalence.getId());
        return CompletableFuture.completedFuture(
            new EquivalenceDetailsDTO(
                responseMapper.toEquivalenceResponseDTO(equivalence),
                equivalence.getSubjects().stream().map(Subject::getId).toList()
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, EquivalenceUpdateDTO data) {
        log.info("[API] Atualizando equivalência com ID: {}", id);
        Equivalence equivalence = finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found");
            
        equivalenceR.save(equivalence);
        log.info("[API] Equivalência atualizada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Equivalence");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.warn("[API] Solicitada exclusão da equivalência com ID: {}", id);
        finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found");
        
        equivalenceR.deleteById(id);
        log.info("[API] Equivalência deletada com sucesso, ID: {}", id);
        return CompletableFuture.completedFuture("Deleted Equivalence");
    }

}
