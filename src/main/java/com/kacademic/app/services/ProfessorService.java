package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademic.app.dto.professor.ProfessorRequestDTO;
import com.kacademic.app.dto.professor.ProfessorResponseDTO;
import com.kacademic.app.dto.professor.ProfessorUpdateDTO;
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.app.mapper.RequestMapper;
import com.kacademic.app.mapper.ResponseMapper;
import com.kacademic.domain.models.Professor;
import com.kacademic.domain.repositories.ProfessorRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProfessorService {
        
    private final ProfessorRepository professorR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(ProfessorRequestDTO data) {
        Professor professor = requestMapper.toProfessor(data);

        professorR.save(professor);
        return CompletableFuture.completedFuture("Professor successfully Created: " + professor.getId());
    }

    @Async
    public CompletableFuture<List<ProfessorResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(professorR.findAll(), responseMapper::toProfessorResponseDTO));
    }

    @Async
    public CompletableFuture<ProfessorResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.completedFuture(responseMapper.toProfessorResponseDTO(finder.findByIdOrThrow(professorR.findById(id), "Professor not Found")));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, ProfessorUpdateDTO data) {
        Professor professor = finder.findByIdOrThrow(professorR.findById(id), "Professor not Found");

        data.wage().ifPresent(professor::setWage);
        
        professorR.save(professor);
        return CompletableFuture.completedFuture("Updated Professor");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(professorR.findById(id), "Professor not Found");
        
        professorR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Professor");
    }

}