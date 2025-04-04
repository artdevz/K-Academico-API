package com.kacademic.app.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
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

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final StudentRepository studentR;
    private final CourseRepository courseR;
    private final RoleRepository roleR;

    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(StudentRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
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
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<StudentResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return studentR.findAll().stream()
                .map(student -> new StudentResponseDTO(
                    student.getId(),                
                    student.getCourse().getId(),
                    student.getEnrollment(),
                    student.getName(),
                    student.getEmail(),
                    student.getAverage()
                ))
                .collect(Collectors.toList()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<StudentDetailsDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
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
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, StudentUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Student student = studentR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found"));
                
            studentR.save(student);
            return "Updated Student";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            if (!studentR.findById(id).isPresent()) 
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found");
            
            studentR.deleteById(id);
            return "Deleted Student";
        }, taskExecutor);
    }

    // Enrollment
    private String generateEnrollment(Course course) {
        return getYear() + getSemester() + course.getCode() + "999" + getRandomNumber(); // Year + Semester + CourseId + ShiftId + RandomNumber 
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