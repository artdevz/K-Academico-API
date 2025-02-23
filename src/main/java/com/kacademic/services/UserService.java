package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.user.UserRequestDTO;
import com.kacademic.dto.user.UserResponseDTO;
import com.kacademic.exceptions.DuplicateValueException;
import com.kacademic.models.User;
import com.kacademic.repositories.UserRepository;
import com.kacademic.validators.PasswordValidator;

@Service
public class UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserRepository userR;    

    public UserService(UserRepository userR) {
        this.userR = userR;
    }

    public void create(UserRequestDTO data) {
         
        validateEmail(data.email());

        User user = new User(            
            data.name(),
            data.email(),
            passwordEncoder.encode(data.password())
        );
        
        userR.save(user);

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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found."));
        
        return new UserResponseDTO(
            user.getId(),            
            user.getName(),
            user.getEmail()
        );
    }

    public User update(UUID id, Map<String, Object> fields) {

        Optional<User> existingUser = userR.findById(id);
    
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            PasswordValidator passwordValidator = new PasswordValidator();
    
            fields.forEach((key, value) -> {
                switch (key) {

                    case "name":
                        String name = (String) value;
                        user.setName(name);
                        break;

                    case "password":
                        String password = (String) value;
                        
                        if (!passwordValidator.isValid(password, null)) 
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The password must contain at least one lowercase letter, one uppercase letter, one number, one special character and be between 8 and 32 characters long.");
                        
                        user.setPassword(passwordEncoder.encode(password));
                        break;  

                    default:
                        Field field = ReflectionUtils.findField(User.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, user, value);
                        }
                        break;
                }
            });
            
            return userR.save(user);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found.");
        
    }

    public void delete(UUID id) {

        if (!userR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not Found.");
        userR.deleteById(id);

    }
    
    private void validateEmail(String email) {

        if (userR.findByEmail(email) != null)
            throw new DuplicateValueException("Email already being used.");

    }

}
