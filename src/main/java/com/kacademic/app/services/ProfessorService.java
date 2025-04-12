package com.kacademic.app.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.professor.ProfessorRequestDTO;
import com.kacademic.app.dto.professor.ProfessorResponseDTO;
import com.kacademic.app.dto.professor.ProfessorUpdateDTO;
import com.kacademic.domain.models.Professor;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.repositories.ProfessorRepository;
import com.kacademic.domain.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProfessorService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private final ProfessorRepository professorR;
    private final RoleRepository roleR;

    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(ProfessorRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Professor professor = new Professor(
                data.user().name(),
                data.user().email(),
                passwordEncoder.encode(data.user().password()),
                findRoles(data.user().roles()),                
                data.wage()
            );
    
            professorR.save(professor);
            return "Professor successfully Created: " + professor.getId();
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<ProfessorResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return professorR.findAll().stream()
                .map(professor -> new ProfessorResponseDTO(
                    professor.getId(),
                    professor.getName(),
                    professor.getEmail(),
                    professor.getWage()
                ))
                .collect(Collectors.toList()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<ProfessorResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Professor professor = findProfessor(id);
    
            return new ProfessorResponseDTO(
                professor.getId(),
                professor.getName(),
                professor.getEmail(),
                professor.getWage()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, ProfessorUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Professor professor = findProfessor(id);
    
            data.wage().ifPresent(professor::setWage);
            
            professorR.save(professor);
            return "Updated Professor";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            findProfessor(id);
            
            professorR.deleteById(id);
            return "Deleted Professor";
        }, taskExecutor);
    }

    private Professor findProfessor(UUID id) {
        return professorR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not Found"));
    }

    private Set<Role> findRoles(Set<UUID> roles) {
        return roles.stream()
        .map(roleId -> roleR.findById(roleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
        .collect(Collectors.toSet());
    }

}