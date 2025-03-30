package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.dto.user.UserResponseDTO;
import com.kacademic.app.dto.user.UserUpdateDTO;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.RoleRepository;
import com.kacademic.domain.repositories.UserRepository;

@Service
public class UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userR;
    private final RoleRepository roleR;

    public UserService(UserRepository userR, RoleRepository roleR) {
        this.userR = userR;
        this.roleR = roleR;
    }

    public String createAsync(UserRequestDTO data) {
         
        validateEmail(data.email());

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
    }

    public List<UserResponseDTO> readAllAsync() {
        return userR.findAll().stream()
            .map(user -> new UserResponseDTO(
                user.getId(),                
                user.getName(),
                user.getEmail(),
                user.getRoles()
            ))
            .collect(Collectors.toList());
    }

    public UserResponseDTO readByIdAsync(UUID id) {
        User user = userR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
        
        return new UserResponseDTO(
            user.getId(),            
            user.getName(),
            user.getEmail(),
            user.getRoles()
        );
    }

    public String updateAsync(UUID id, UserUpdateDTO data) {
        User user = userR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found"));
        
        data.name().ifPresent(user::setName);
        data.password().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
            
        userR.save(user);
        return "Updated User";
    }

    public String deleteAsync(UUID id) {
        if (!userR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found");
        
        userR.deleteById(id);
        return "Deleted User";
    }

    public String createUserTesterSync() {
        userR.save(new User());
        return "Created User Tester";
    }
    
    private void validateEmail(String email) {
        if (userR.findByEmail(email).isPresent()) 
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already being used");
    }
}