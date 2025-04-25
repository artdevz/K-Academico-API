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
import com.kacademic.app.dto.professor.ProfessorRequestDTO;
import com.kacademic.app.dto.student.StudentRequestDTO;
import com.kacademic.app.dto.subject.SubjectRequestDTO;
import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.app.services.EnrollmentGeneratorService;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Equivalence;
import com.kacademic.domain.models.Professor;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.models.Student;
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
    
    private final EnrollmentGeneratorService enrollmentGS;

    public Course toCourse(CourseRequestDTO data) {
        return new Course(
            data.name(),
            data.code(),
            data.description()
        );
    }

    public User toUser(UserRequestDTO data) {
        return new User(
            data.name(),
            data.email(),
            passwordEncoder.encode(data.password()),
            findRoles(data.roles())
        );
    }

    public Subject toSubject(SubjectRequestDTO data) {
        return new Subject(
            data.name(),
            data.description(),
            data.duration(),
            data.semester(),
            data.isRequired(),
            finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found"),
            findEquivalences(data.prerequisites())
        );
    }

    public Student toStudent(StudentRequestDTO data) {
        return new Student(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),
            findRoles(data.user().roles()),
            enrollmentGS.generate(finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found").getCode()),
            finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found")
        );
    }

    public Professor toProfessor(ProfessorRequestDTO data) {
        return new Professor(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),
            findRoles(data.user().roles()),
            data.wage()
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
