package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.dto.user.UserResponseDTO;
import com.kacademic.dto.user.UserUpdateDTO;
import com.kacademic.exceptions.DuplicateValueException;
import com.kacademic.models.User;
import com.kacademic.repositories.UserRepository;
import com.kacademic.validators.PasswordValidator;

@Service
public class UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userR;
    
    private final String entity = "User";

    public UserService(UserRepository userR) {
        this.userR = userR;
    }

    public String create(UserRequestDTO data) {
         
        validateEmail(data.email());

        User user = new User(            
            data.name(),
            data.email(),
            passwordEncoder.encode(data.password())
        );
        
        userR.save(user);
        return "Created" + entity;

    }

    public List<UserResponseDTO> readAll() {

        return userR.findAll().stream()
            .map(user -> new UserResponseDTO(
                user.getId(),                
                user.getName(),
                user.getEmail()
            ))
            .collect(Collectors.toList());
    }

    public UserResponseDTO readById(UUID id) {

        User user = userR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
        
        return new UserResponseDTO(
            user.getId(),            
            user.getName(),
            user.getEmail()
        );
    }

    public String update(UUID id, UserUpdateDTO data) {

        User user = userR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
        
        data.name().ifPresent(user::setName);
        data.password().ifPresent(password -> validatePassword(user, password.intern())); // Não faço menor ideia do pq funcionou
            
        userR.save(user);
        return "Updated" + entity;
                
    }

    public String delete(UUID id) {

        if (!userR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found.");
        
        userR.deleteById(id);
        return "Deleted " + entity;

    }
    
    private void validateEmail(String email) {

        if (userR.findByEmail(email) != null)
            throw new DuplicateValueException("Email already being used.");

    }

    private void validatePassword(User user, String password) {

        PasswordValidator passwordValidator = new PasswordValidator();

        if (!passwordValidator.isValid(password, null))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            "Password Invalid.");

        user.setPassword(passwordEncoder.encode(password));

    }

}
