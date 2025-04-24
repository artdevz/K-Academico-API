package com.kacademic.app.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.app.dto.subject.SubjectRequestDTO;
import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Equivalence;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.models.Subject;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.CourseRepository;
import com.kacademic.domain.repositories.EquivalenceRepository;
import com.kacademic.domain.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RequestMapper {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EntityFinder finder;

    private final CourseRepository courseR;
    private final EquivalenceRepository equivalenceR;
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

    public Subject toSubject(SubjectRequestDTO subjectRequestDTO) {
        return new Subject(
            subjectRequestDTO.name(),
            subjectRequestDTO.description(),
            subjectRequestDTO.duration(),
            subjectRequestDTO.semester(),
            subjectRequestDTO.isRequired(),
            finder.findByIdOrThrow(courseR.findById(subjectRequestDTO.course()), "Course not Found"),
            findEquivalences(subjectRequestDTO.prerequisites())
        );
    }

    private List<Equivalence> findEquivalences(List<UUID> equivalences) {
        return equivalences.stream()
            .map(id -> finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found"))
        .collect(Collectors.toList());
    }

    private Set<Role> findRoles(Set<UUID> roles) {
        return roles.stream()
        .map(roleId -> roleR.findById(roleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
        .collect(Collectors.toSet());
    }

}
