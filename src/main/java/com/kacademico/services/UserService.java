package com.kacademico.services;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import com.kacademico.dtos.user.UserRequestDTO;
import com.kacademico.dtos.user.UserResponseDTO;
import com.kacademico.exceptions.DuplicateValueException;
import com.kacademico.models.User;
import com.kacademico.repositories.UserRepository;

@Service
public class UserService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // private static int enrollmentId = 1000000; // BUG: Não pode desligar o servidor
   
    private final UserRepository userR;    

    public UserService(UserRepository userR) {
        this.userR = userR;
    }

    public void create(UserRequestDTO data) {
         
        validateEmail(data.email());

        User user = new User(
            generateEnrollment(),
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
                user.getEnrollment(),
                user.getName(),
                user.getEmail()
            ))
            .collect(Collectors.toList());
    }

    public UserResponseDTO readById(UUID id) {

        User user = userR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        return new UserResponseDTO(
            user.getId(),
            user.getEnrollment(),
            user.getName(),
            user.getEmail()
        );
    }

    public User update(UUID id, Map<String, Object> fields) {

        Optional<User> existingUser = userR.findById(id);
    
        if (existingUser.isPresent()) {
            User user = existingUser.get();
    
            fields.forEach((key, value) -> {
                switch (key) {

                    case "name":
                        String name = (String) value;
                        user.setName(name);
                        break;

                    case "password":
                        String password = (String) value;
                        // validatePassword(password);
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
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        
    }

    public void delete(UUID id) {

        if (!userR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        userR.deleteById(id);

    }

    // Enrollment:
    private String generateEnrollment() {
        // 2024 + 1 ou 2 + 0000000
        return getYear() + getSemester() + getRandomNumber();

    }

    private String getYear() {

        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));

    }

    private String getSemester() {

        final int MID_OF_YEAR = 6;
        return (LocalDate.now().getMonthValue() <= MID_OF_YEAR) ? "1" : "2";

    }
    
    private String getRandomNumber() {

        return "0000000"; //String.valueOf(++enrollmentId);

    }

    // Email:

    private void validateEmail(String email) {

        if (userR.findByEmail(email) != null)
            throw new DuplicateValueException("Email já está sendo utilizado.");

    }

}
