package com.kacademic.app.services;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.role.RoleRequestDTO;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.AppRoleRepository;
import com.kacademic.domain.repositories.UserRepository;

@Service
public class InitService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private final UserRepository userR;
    private final AppRoleRepository roleR;

    public InitService(UserRepository userR, AppRoleRepository roleR) {
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
            passwordEncoder.encode("4bcdefg!"),
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

        roleR.save(role);

        return CompletableFuture.completedFuture("Created Role");

    }

}
