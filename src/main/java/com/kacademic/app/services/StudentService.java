package com.kacademic.app.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.app.dto.student.StudentDetailsDTO;
import com.kacademic.app.dto.student.StudentRequestDTO;
import com.kacademic.app.dto.student.StudentResponseDTO;
import com.kacademic.app.dto.student.StudentUpdateDTO;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Student;
import com.kacademic.domain.repositories.CourseRepository;
import com.kacademic.domain.repositories.RoleRepository;
import com.kacademic.domain.repositories.StudentRepository;

@Service
public class StudentService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final StudentRepository studentR;
    private final CourseRepository courseR;
    private final RoleRepository roleR;

    public StudentService(StudentRepository studentR, CourseRepository courseR, RoleRepository roleR) {
        this.studentR = studentR;
        this.courseR = courseR;
        this.roleR = roleR;
    }

    public String createAsync(StudentRequestDTO data) {

        Student student = new Student(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),
            data.user().roles().stream()
                .map(roleId -> roleR.findById(roleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
                .collect(Collectors.toSet()),
            courseR.findById(data.course()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found")),
            generateEnrollment(courseR.findById(data.course()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found")))
        );

        studentR.save(student);
        return "Created Student";
        
    }

    public List<StudentResponseDTO> readAllAsync() {

        return studentR.findAll().stream()
            .map(student -> new StudentResponseDTO(
                student.getId(),                
                student.getCourse().getId(),
                student.getEnrollment(),
                student.getName(),
                student.getEmail(),
                student.getAverage()
            ))
            .collect(Collectors.toList());
    }

    public StudentDetailsDTO readByIdAsync(UUID id) {

        Student student = studentR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found"));
        
        return new StudentDetailsDTO(
            new StudentResponseDTO(
                student.getId(),            
                student.getCourse().getId(),
                student.getEnrollment(),
                student.getName(),
                student.getEmail(),
                student.getAverage()
            ),
            student.getEnrollees().stream().map(enrollee -> new EnrolleeResponseDTO(
                enrollee.getId(),
                enrollee.getStudent().getId(),
                enrollee.getGrade().getId(),
                enrollee.getAbsences(),
                enrollee.getAverage(),
                enrollee.getStatus()
            )).collect(Collectors.toList())
        );
    }

    public String updateAsync(UUID id, StudentUpdateDTO data) {

        Student student = studentR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found"));
            
        studentR.save(student);
        return "Updated Student";
        
    }

    public String deleteAsync(UUID id) {

        if (!studentR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found");
        
        studentR.deleteById(id);
        return "Deleted Student";

    }

    // Enrollment
    private String generateEnrollment(Course course) {

        // Year + Semester + CourseId + ShiftId + RandomNumber        
        return getYear() + getSemester() + course.getCode() + "999" + getRandomNumber();

    }

    private String getYear() {

        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));

    }

    private String getSemester() {

        final int MID_OF_YEAR = 6;
        return (LocalDate.now().getMonthValue() <= MID_OF_YEAR) ? "1" : "2";

    }

    private String getRandomNumber() {

        return "0000";

    }

}