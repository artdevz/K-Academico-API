package com.kacademic.app.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.dto.user.UserResponseDTO;
import com.kacademic.app.dto.user.UserUpdateDTO;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.RoleRepository;
import com.kacademic.domain.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userR;
    private final RoleRepository roleR;

    @Async
    public CompletableFuture<String> createInitialAdmin() {
        Role adminRole = roleR.findByName("ADMIN").orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ADMIN isn't created yet"));
        ensureEmailIsUnique("admin@gmail.com");

        User admin = new User(
            "K-Academic Admin",
            "admin@gmail.com",
            passwordEncoder.encode("4bcdefG!"),
            Set.of(adminRole)
        );

        userR.save(admin);
        return CompletableFuture.completedFuture("Admin successfully Created: " + admin.getId());
    }

    @Async
    public CompletableFuture<String> createAsync(UserRequestDTO data) {
        ensureEmailIsUnique(data.email());

        User user = new User(            
            data.name(),
            data.email(),
            passwordEncoder.encode(data.password()),
            findRoles(data.roles())
        );
        
        userR.save(user);
        return CompletableFuture.completedFuture("User successfully Created: " + user.getId());
    }

    @Async
    public CompletableFuture<List<UserResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(
            userR.findAll().stream()
            .map(user -> new UserResponseDTO(
                user.getId(),                
                user.getName(),
                user.getEmail(),
                user.getRoles()
            ))
            .collect(Collectors.toList()
        ));
    }

    @Async
    public CompletableFuture<UserResponseDTO> readByIdAsync(UUID id) {
        User user = findUser(id);
        
        return CompletableFuture.completedFuture(
            new UserResponseDTO(
                user.getId(),            
                user.getName(),
                user.getEmail(),
                user.getRoles()
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, UserUpdateDTO data) {
        User user = findUser(id);
        
        data.name().ifPresent(user::setName);
        data.password().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
            
        userR.save(user);
        return CompletableFuture.completedFuture("Updated User");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        findUser(id);
        
        userR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted User");
    }

    private User findUser(UUID id) {
        return userR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
    }

    private Set<Role> findRoles(Set<UUID> roles) {
        return roles.stream()
        .map(roleId -> roleR.findById(roleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
        .collect(Collectors.toSet());
    }
    
    private void ensureEmailIsUnique(String email) {
        if (userR.findByEmail(email).isPresent()) 
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already being used");
    }

}