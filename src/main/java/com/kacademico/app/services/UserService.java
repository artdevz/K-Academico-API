package com.kacademico.app.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.user.UserRequestDTO;
import com.kacademico.app.dto.user.UserResponseDTO;
import com.kacademico.app.dto.user.UserUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Role;
import com.kacademico.domain.models.User;
import com.kacademico.domain.repositories.IRoleRepository;
import com.kacademico.domain.repositories.IUserRepository;
import com.kacademico.shared.utils.EnsureUniqueUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final IUserRepository userR;
    private final EntityFinder finder;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final IRoleRepository roleR;

    @Async
    public CompletableFuture<String> createInitialAdmin() {
        Role adminRole = roleR.findByName("ADMIN").orElseGet(() -> {
            Role role = new Role(null, "ADMIN", "All authorities in K-Academico System");
            roleR.save(role);
            return role;
        });
        
        EnsureUniqueUtil.ensureUnique(() -> userR.findByEmail("admin@gmail.com"), () -> "An user with email " + "admin@gmail.com" + " already exists");

        User admin = new User(
            null,
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
        EnsureUniqueUtil.ensureUnique(() -> userR.findByEmail(data.email()), () -> "An user with email " + data.email() + " already exists");

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

}