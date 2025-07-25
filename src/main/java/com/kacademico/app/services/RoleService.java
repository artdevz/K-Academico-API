package com.kacademico.app.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.role.RoleRequestDTO;
import com.kacademico.app.dto.role.RoleResponseDTO;
import com.kacademico.domain.models.Role;
import com.kacademico.domain.repositories.IRoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoleService {
    
    private final IRoleRepository roleR;

    @Async
    public CompletableFuture<String> createAsync(RoleRequestDTO data) {
        Role role = new Role(
            null,
            data.name(),
            data.description()
        );

        Role saved = roleR.save(role);
        return CompletableFuture.completedFuture("Role successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<RoleResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(roleR.findAll().stream()
            .map(role -> new RoleResponseDTO(
                role.getId(),
                role.getName(),
                role.getDescription()
            ))
            .collect(Collectors.toList()
        ));
    }

}
