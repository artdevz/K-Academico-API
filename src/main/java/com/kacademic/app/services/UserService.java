package com.kacademic.app.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.dto.user.UserResponseDTO;
import com.kacademic.app.dto.user.UserUpdateDTO;
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.app.mapper.RequestMapper;
import com.kacademic.app.mapper.ResponseMapper;
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
    private final EntityFinder finder;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final RoleRepository roleR;

    @Async
    public CompletableFuture<String> createInitialAdmin() {
        Role adminRole = roleR.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role("ADMIN", "All authorities in K-Academico System");
            roleR.save(role);
            return role;
        });
        
        ensureEmailIsUnique("admin@gmail.com");

        User admin = new User(
            "K-Academico Admin",
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

        User user = requestMapper.toUser(data);
        
        userR.save(user);
        return CompletableFuture.completedFuture("User successfully Created: " + user.getId());
    }

    @Async
    public CompletableFuture<List<UserResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(userR.findAll(), responseMapper::toUserResponseDTO));
    }

    @Async
    public CompletableFuture<UserResponseDTO> readByIdAsync(UUID id) {
        User user = finder.findByIdOrThrow(userR.findById(id), "User not Found");
        
        return CompletableFuture.completedFuture(responseMapper.toUserResponseDTO(user));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, UserUpdateDTO data) {
        User user = finder.findByIdOrThrow(userR.findById(id), "User not Found");
        
        data.name().ifPresent(user::setName);
        data.password().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
            
        userR.save(user);
        return CompletableFuture.completedFuture("Updated User");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(userR.findById(id), "User not Found");
        
        userR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted User");
    }
    
    private void ensureEmailIsUnique(String email) {
        if (userR.findByEmail(email).isPresent()) 
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already being used");
    }

}