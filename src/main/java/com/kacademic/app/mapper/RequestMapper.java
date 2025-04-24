package com.kacademic.app.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RequestMapper {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final RoleRepository roleR;
    
    public Course toCourse(CourseRequestDTO courseRequestDTO) {
        return new Course(
            courseRequestDTO.name(),
            courseRequestDTO.code(),
            courseRequestDTO.description()
        );
    }

    public User toUser(UserRequestDTO userRequestDTO) {
        return new User(
            userRequestDTO.name(),
            userRequestDTO.email(),
            passwordEncoder.encode(userRequestDTO.password()),
            findRoles(userRequestDTO.roles())
        );
    }

    private Set<Role> findRoles(Set<UUID> roles) {
        return roles.stream()
        .map(roleId -> roleR.findById(roleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
        .collect(Collectors.toSet());
    }

}
