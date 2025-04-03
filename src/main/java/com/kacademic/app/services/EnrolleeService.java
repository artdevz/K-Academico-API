package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
// import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.attendance.AttendanceResponseDTO;
import com.kacademic.app.dto.enrollee.EnrolleeDetailsDTO;
import com.kacademic.app.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.app.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.app.dto.enrollee.EnrolleeUpdateDTO;
import com.kacademic.app.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.domain.models.Enrollee;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.GradeRepository;
import com.kacademic.domain.repositories.StudentRepository;

@Service
public class EnrolleeService {
    
    private final EnrolleeRepository enrolleeR;
    private final StudentRepository studentR;
    private final GradeRepository gradeR;

    public EnrolleeService(EnrolleeRepository enrolleeR, StudentRepository studentR, GradeRepository gradeR) {
        this.enrolleeR = enrolleeR;
        this.studentR = studentR;
        this.gradeR = gradeR;
    }
    
    // @Async
    public String createAsync(EnrolleeRequestDTO data) {

        Enrollee enrollee = new Enrollee(
            studentR.findById(data.student()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not Found")),
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found"))
        );

        enrollee.getGrade().setCurrentStudents(
            enrollee.getGrade().getCurrentStudents() + 1 
        );
        enrolleeR.save(enrollee);
        return "Created Enrollee";
    }

    // @Async
    public List<EnrolleeResponseDTO> readAllAsync() {
        return (
            enrolleeR.findAll().stream()
            .map(enrollee -> new EnrolleeResponseDTO(
                enrollee.getId(),
                enrollee.getStudent().getId(),
                enrollee.getGrade().getId(),
                enrollee.getAbsences(),
                enrollee.getAverage(),
                enrollee.getStatus()
            ))
            .collect(Collectors.toList()));
    }

    // @Async
    public EnrolleeDetailsDTO readByIdAsync(UUID id) {

        Enrollee enrollee = enrolleeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
        
        return (
            new EnrolleeDetailsDTO(
                new EnrolleeResponseDTO(
                    enrollee.getId(),
                    enrollee.getStudent().getId(),
                    enrollee.getGrade().getId(),
                    enrollee.getAbsences(),
                    enrollee.getAverage(),
                    enrollee.getStatus()
                ),
                enrollee.getEvaluations().stream().map(evaluation -> new EvaluationResponseDTO(
                    evaluation.getId(),
                    evaluation.getEnrollee().getId(),
                    evaluation.getExam().getGrade().getId(),
                    evaluation.getExam().getId(),
                    evaluation.getScore()
                )).collect(Collectors.toList()),
                enrollee.getAttendances().stream().map(attendance -> new AttendanceResponseDTO(
                    attendance.getId(),
                    attendance.getEnrollee().getId(),
                    attendance.getLesson().getId(),
                    attendance.isAbsent()
                )).collect(Collectors.toList())
        ));
    }

    // @Async
    public String updateAsync(UUID id, EnrolleeUpdateDTO data) {

        Enrollee enrollee = enrolleeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
            
        enrolleeR.save(enrollee);
        return "Updated Enrollee";
    }

    // @Async
    public String deleteAsync(UUID id) {

        if (!enrolleeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found");

        enrolleeR.findById(id).get().getGrade().setCurrentStudents(
            enrolleeR.findById(id).get().getGrade().getCurrentStudents() - 1
        );
        enrolleeR.deleteById(id);
        return "Deleted Enrollee";
    }
}