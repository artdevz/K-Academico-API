package com.kacademic.services;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.role.RoleRequestDTO;
import com.kacademic.models.Role;
import com.kacademic.models.User;
import com.kacademic.repositories.RoleRepository;
import com.kacademic.repositories.UserRepository;

@Service
public class InitService {
    
    private final UserRepository userR;
    private final RoleRepository roleR;

    public InitService(UserRepository userR, RoleRepository roleR) {
        this.userR = userR;
        this.roleR = roleR;
    }

    @Async
    public CompletableFuture<String> initAdminAsync() {

        String adminEmail = "admin@gmail.com";
        Role adminRole = roleR.findByName("ADMIN").orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ADMIN isn't created yet"));

        User admin = new User(
            "Admin",
            adminEmail,
            "4bcdefg!",
            Set.of(adminRole)
        );

        userR.save(admin);
        return CompletableFuture.completedFuture("Created Admin");

    }

    @Async
    public CompletableFuture<String> initRolesAsync(RoleRequestDTO data) {

        if ( roleR.findByName(data.name()).isPresent() ) throw new ResponseStatusException(HttpStatus.CONFLICT, "Role name already being used");

        Role role = new Role(
            data.name(),
            data.description()
        );

        System.out.println("Role name: " + role.getName() + "\nRole Description: " + role.getDescription() + "\nRole Authorities: " + role.getAuthority() + "\nRole ID: " + role.getId());

        roleR.save(role);

        return CompletableFuture.completedFuture("Created Role");

    }

}
