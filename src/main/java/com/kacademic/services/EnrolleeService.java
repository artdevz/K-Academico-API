package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.attendance.AttendanceResponseDTO;
import com.kacademic.dto.enrollee.EnrolleeDetailsDTO;
import com.kacademic.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.dto.enrollee.EnrolleeUpdateDTO;
import com.kacademic.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.models.Enrollee;
import com.kacademic.repositories.EnrolleeRepository;
import com.kacademic.repositories.GradeRepository;
import com.kacademic.repositories.StudentRepository;

@Service
public class EnrolleeService {
    
    private final EnrolleeRepository enrolleeR;
    private final StudentRepository studentR;
    private final GradeRepository gradeR;

    private final String entity = "Enrollee";

    public EnrolleeService(EnrolleeRepository enrolleeR, StudentRepository studentR, GradeRepository gradeR) {
        this.enrolleeR = enrolleeR;
        this.studentR = studentR;
        this.gradeR = gradeR;
    }
    
    public String create(EnrolleeRequestDTO data) {

        Enrollee enrollee = new Enrollee(
            studentR.findById(data.student()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student")),
            studentR.findById(data.student()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student.Transcript")).getTranscript(),
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade"))
        );

        enrollee.getGrade().setCurrentStudents( // Adiciona 1 Estudante na Turma
            enrollee.getGrade().getCurrentStudents() + 1 
        );
        enrolleeR.save(enrollee);
        return "Created" + entity;

    }

    public List<EnrolleeResponseDTO> readAll() {

        return enrolleeR.findAll().stream()
            .map(enrollee -> new EnrolleeResponseDTO(
                enrollee.getId(),
                enrollee.getStudent().getId(),
                // enrollee.getGrade() != null ? enrollee.getGrade().getSubject().getName() : "Unavailable",
                enrollee.getGrade().getId(),
                enrollee.getAbsences(),
                enrollee.getAvarage(),
                enrollee.getStatus()
            ))
            .collect(Collectors.toList());

    }

    public EnrolleeDetailsDTO readById(UUID id) {

        Enrollee enrollee = enrolleeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found."));
        
        return new EnrolleeDetailsDTO(
            new EnrolleeResponseDTO(
                enrollee.getId(),
                enrollee.getStudent().getId(),
                enrollee.getGrade().getId(),
                enrollee.getAbsences(),
                enrollee.getAvarage(),
                enrollee.getStatus()
                ),
                
            enrollee.getEvaluations().stream().map(evaluation -> new EvaluationResponseDTO(
                evaluation.getId(),
                evaluation.getEnrollee().getId(),
                evaluation.getExam().getGrade().getId(),
                evaluation.getExam().getId(),
                evaluation.getScore()
                ))
                .collect(Collectors.toList()),

            enrollee.getAttendances().stream().map(attendance -> new AttendanceResponseDTO(
                attendance.getId(),
                attendance.getEnrollee().getId(),
                attendance.getLesson().getId(),
                attendance.isAbsent()
                )).collect(Collectors.toList())
            
        );

    }

    public String update(UUID id, EnrolleeUpdateDTO data) {

        Enrollee enrollee = enrolleeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
            
        enrolleeR.save(enrollee);
        return "Updated" + entity;
        
    }

    public String delete(UUID id) {

        if (!enrolleeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found.");

        enrolleeR.findById(id).get().getGrade().setCurrentStudents( // Remove 1 Estudante na Turma
            enrolleeR.findById(id).get().getGrade().getCurrentStudents() - 1
        );
        enrolleeR.deleteById(id);
        return "Deleted" + entity;

    }

}
