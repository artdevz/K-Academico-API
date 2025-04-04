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

    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> createInitialAdmin() {
        return CompletableFuture.supplyAsync(() -> {
            Role adminRole = roleR.findByName("ADMIN").orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ADMIN isn't created yet"));
    
            User admin = new User(
                "K-Academic Admin",
                "admin@gmail.com",
                passwordEncoder.encode("4bcdefG!"),
                Set.of(adminRole)
            );
    
            userR.save(admin);
            return "Created Admin";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(UserRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            ensureEmailIsUnique(data.email());
    
            User user = new User(            
                data.name(),
                data.email(),
                passwordEncoder.encode(data.password()),
                data.roles().stream()
                    .map(roleId -> roleR.findById(roleId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
                    .collect(Collectors.toSet())
            );
            
            userR.save(user);
            return "Created User";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<UserResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return userR.findAll().stream()
                .map(user -> new UserResponseDTO(
                    user.getId(),                
                    user.getName(),
                    user.getEmail(),
                    user.getRoles()
                ))
                .collect(Collectors.toList()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<UserResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            User user = userR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
            
            return new UserResponseDTO(
                user.getId(),            
                user.getName(),
                user.getEmail(),
                user.getRoles()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, UserUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            User user = userR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
            
            data.name().ifPresent(user::setName);
            data.password().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
                
            userR.save(user);
            return "Updated User";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            if (!userR.findById(id).isPresent()) 
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found");
            
            userR.deleteById(id);
            return "Deleted User";
        }, taskExecutor);
    }
    
    private void ensureEmailIsUnique(String email) {
        if (userR.findByEmail(email).isPresent()) 
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already being used");
    }
}