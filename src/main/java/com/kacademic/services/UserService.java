package com.kacademic.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.dto.user.UserResponseDTO;
import com.kacademic.dto.user.UserUpdateDTO;
import com.kacademic.models.User;
import com.kacademic.repositories.UserRepository;

@Service
public class UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userR;
    
    private final String entity = "User";

    public UserService(UserRepository userR) {
        this.userR = userR;
    }

    @Async
    public CompletableFuture<String> createAsync(UserRequestDTO data) {
         
        validateEmail(data.email());

        User user = new User(            
            data.name(),
            data.email(),
            passwordEncoder.encode(data.password())
        );
        
        userR.save(user);
        return CompletableFuture.completedFuture("Created " + entity);

    }

    @Async
    public CompletableFuture<List<UserResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
            userR.findAll().stream()
            .map(user -> new UserResponseDTO(
                user.getId(),                
                user.getName(),
                user.getEmail()
            ))
            .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<UserResponseDTO> readByIdAsync(UUID id) {

        User user = userR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
        
        return CompletableFuture.completedFuture(
            new UserResponseDTO(
                user.getId(),            
                user.getName(),
                user.getEmail()
        ));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, UserUpdateDTO data) {

        User user = userR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
        
        data.name().ifPresent(user::setName);
        data.password().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
            
        userR.save(user);
        return CompletableFuture.completedFuture("Updated " + entity);
                
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!userR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found");
        
        userR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted " + entity);

    }

    public String createUserTesterSync() {
        userR.save(new User("UserTest", "test@gmail.com", "4bcdefg!test"));
        return "Created User Tester";
    }
    
    private void validateEmail(String email) {
        if (!(userR.findByEmail(email).equals(Optional.empty()))) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already being used");
    }

}
