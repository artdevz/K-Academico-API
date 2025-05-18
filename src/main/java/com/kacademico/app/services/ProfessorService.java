package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.professor.ProfessorRequestDTO;
import com.kacademico.app.dto.professor.ProfessorResponseDTO;
import com.kacademico.app.dto.professor.ProfessorUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Professor;
import com.kacademico.domain.repositories.ProfessorRepository;
import com.kacademico.domain.repositories.IUserRepository;
import com.kacademico.shared.utils.EnsureUniqueUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProfessorService {
        
    private final ProfessorRepository professorR;
    private final IUserRepository userR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(ProfessorRequestDTO data) {
        EnsureUniqueUtil.ensureUnique(() -> userR.findByEmail(data.user().email()), () -> "An user with email " + data.user().email() + " already exists");

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