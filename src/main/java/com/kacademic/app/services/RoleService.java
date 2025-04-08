package com.kacademic.app.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademic.app.dto.role.RoleRequestDTO;
import com.kacademic.app.dto.role.RoleResponseDTO;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService {
    
    private final RoleRepository roleR;

    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(RoleRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Role role = new Role(
                data.name(),
                data.description()
            );
    
            roleR.save(role);
            return "Role successfully Created: " + role.getId();
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<RoleResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return roleR.findAll().stream()
                .map(role -> new RoleResponseDTO(
                    role.getId(),
                    role.getName(),
                    role.getDescription()
                ))
                .collect(Collectors.toList()
            );
        }, taskExecutor);
    }

}
