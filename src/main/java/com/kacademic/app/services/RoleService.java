package com.kacademic.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kacademic.app.dto.role.RoleRequestDTO;
import com.kacademic.app.dto.role.RoleResponseDTO;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.repositories.RoleRepository;

@Service
public class RoleService {
    
    private final RoleRepository roleR;

    public RoleService(RoleRepository roleR) {
        this.roleR = roleR;
    }

    public String createAsync(RoleRequestDTO data) {

        Role role = new Role(
            data.name(),
            data.description()
        );

        roleR.save(role);
        return "Created Role";

    }

    public List<RoleResponseDTO> readAllAsync() {

        return roleR.findAll().stream()
            .map(role -> new RoleResponseDTO(
                role.getId(),
                role.getName(),
                role.getDescription()
            ))
            .collect(Collectors.toList());

    }

}
