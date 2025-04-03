package com.kacademic.app.services;

import org.springframework.stereotype.Service;

import com.kacademic.app.dto.role.RoleRequestDTO;
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

}
