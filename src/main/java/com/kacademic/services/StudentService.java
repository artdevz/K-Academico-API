package com.kacademic.services;

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

import com.kacademic.dto.student.StudentRequestDTO;
import com.kacademic.dto.student.StudentResponseDTO;
import com.kacademic.enums.EShift;
import com.kacademic.models.Course;
import com.kacademic.models.Student;
import com.kacademic.repositories.StudentRepository;

@Service
public class StudentService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final StudentRepository studentR;

    private final MappingService mapS;

    public StudentService(StudentRepository studentR, MappingService mapS) {
        this.studentR = studentR;
        this.mapS = mapS;
    }

    public void create(StudentRequestDTO data) {

        Student student = new Student(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),
            mapS.findCourseById(data.course()),
            generateEnrollment(mapS.findCourseById(data.course()), data.shift()),
            data.shift()
        );

        studentR.save(student);
        
    }

    public List<StudentResponseDTO> readAll() {

        return studentR.findAll().stream()
            .map(student -> new StudentResponseDTO(
                student.getId(),                
                student.getCourse().getId(),
                student.getEnrollment(),
                student.getName(),
                student.getEmail(),
                student.getAvarage(),
                student.getShift()
            ))
            .collect(Collectors.toList());
    }

    public StudentResponseDTO readById(UUID id) {

        Student student = studentR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudante não encontrado."));
        
        return new StudentResponseDTO(
            student.getId(),            
            student.getCourse().getId(),
            student.getEnrollment(),
            student.getName(),
            student.getEmail(),
            student.getAvarage(),
            student.getShift()
        );
    }

    public Student update(UUID id, Map<String, Object> fields) {

        Optional<Student> existingStudent = studentR.findById(id);
    
        if (existingStudent.isPresent()) {
            Student student = existingStudent.get();
    
            fields.forEach((key, value) -> {
                switch (key) {

                    default:
                        Field field = ReflectionUtils.findField(Student.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, student, value);
                        }
                        break;
                }
            });
            
            return studentR.save(student);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudante não encontrado.");
        
    }

    public void delete(UUID id) {

        if (!studentR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudante não encontrado.");
        studentR.deleteById(id);

    }

    // Enrollment
    private String generateEnrollment(Course course, EShift shift) {

        // Year + Semester + CourseId + ShiftId + RandomNumber        
        return getYear() + getSemester() + course.getCode() + shift.getCode() + getRandomNumber();

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
