package com.kacademic.app.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kacademic.app.dto.course.CourseResponseDTO;
import com.kacademic.app.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.app.dto.professor.ProfessorResponseDTO;
import com.kacademic.app.dto.student.StudentResponseDTO;
import com.kacademic.app.dto.subject.SubjectResponseDTO;
import com.kacademic.app.dto.user.UserResponseDTO;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Enrollee;
import com.kacademic.domain.models.Professor;
import com.kacademic.domain.models.Student;
import com.kacademic.domain.models.Subject;
import com.kacademic.domain.models.User;

@Component
public class ResponseMapper {
    
    public CourseResponseDTO toCourseResponseDTO(Course course) {
        return new CourseResponseDTO(
            course.getId(),
            course.getName(),
            course.getCode(),
            course.getDuration(),
            course.getDescription()
        );
    }

    public List<CourseResponseDTO> toCourseResponseDTOList(List<Course> courses) {
        return courses.stream()
            .map(this::toCourseResponseDTO)
            .collect(Collectors.toList());
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRoles()
        );
    }

    public List<UserResponseDTO> toUserResponseDTOList(List<User> users) {
        return users.stream()
            .map(this::toUserResponseDTO)
            .collect(Collectors.toList());
    }

    public SubjectResponseDTO toSubjectResponseDTO(Subject subject) {
        return new SubjectResponseDTO(
            subject.getId(),
            subject.getCourse().getId(),
            subject.getName(),
            subject.getDescription(),
            subject.getDuration(),
            subject.getSemester(),
            subject.isRequired()
        );
    }

    public List<SubjectResponseDTO> toSubjectResponseDTOList(List<Subject> subjects) {
        return subjects.stream()
            .map(this::toSubjectResponseDTO)
            .collect(Collectors.toList());
    }

    public StudentResponseDTO toStudentResponseDTO(Student student) {
        return new StudentResponseDTO(
            student.getId(),
            student.getCourse().getId(),
            student.getEnrollment().getValue(),
            student.getName(),
            student.getEmail(),
            student.getAverage()
        );
    }

    public List<StudentResponseDTO> toStudentResponseDTOList(List<Student> students) {
        return students.stream()
            .map(this::toStudentResponseDTO)
            .collect(Collectors.toList());
    }

    public ProfessorResponseDTO toProfessorResponseDTO(Professor professor) {
        return new ProfessorResponseDTO(
            professor.getId(),
            professor.getName(),
            professor.getEmail(),
            professor.getWage()
        );
    }

    public List<ProfessorResponseDTO> toProfessorResponseDTOList(List<Professor> professors) {
        return professors.stream()
            .map(this::toProfessorResponseDTO)
            .collect(Collectors.toList());
    }

    public EnrolleeResponseDTO toEnrolleeResponseDTO(Enrollee enrollee) {
        return new EnrolleeResponseDTO(
            enrollee.getId(),
            enrollee.getStudent().getId(),
            enrollee.getGrade().getId(),
            enrollee.getAbsences(),
            enrollee.getAverage(),
            enrollee.getStatus()
        );
    }

    public List<EnrolleeResponseDTO> toEnrolleeResponseDTOList(Set<Enrollee> enrollees) {
        return enrollees.stream()
            .map(this::toEnrolleeResponseDTO)
            .collect(Collectors.toList());
    }

}
