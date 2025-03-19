package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.grade.GradeRequestDTO;
import com.kacademic.dto.grade.GradeResponseDTO;
import com.kacademic.dto.grade.GradeUpdateDTO;
import com.kacademic.models.Grade;
import com.kacademic.repositories.EnrolleeRepository;
import com.kacademic.repositories.GradeRepository;

import jakarta.transaction.Transactional;

@Service
public class GradeService {
    
    private final GradeRepository gradeR;
    private final EnrolleeRepository enrolleeR;
    
    private final MappingService mapS;

    private final String entity = "Grade";

    public GradeService(GradeRepository gradeR, EnrolleeRepository enrolleeR, MappingService mapS) {
        this.gradeR = gradeR;
        this.enrolleeR = enrolleeR;
        this.mapS = mapS;
    }
    
    public String create(GradeRequestDTO data) {

        Grade grade = new Grade(
            mapS.findSubjectById(data.subject()),
            mapS.findProfessorById(data.professor()),
            data.capacity(),
            data.semester(),
            data.locate(),
            data.timetable()
        );

        gradeR.save(grade);
        return "Created" + entity;

    }

    public List<GradeResponseDTO> readAll() {

        return gradeR.findAll().stream()
            .map(grade -> new GradeResponseDTO(
                grade.getId(),
                grade.getSubject().getId(),
                grade.getProfessor().getId(),
                grade.getCapacity(),
                grade.getSemester(),
                grade.getLocate(),
                grade.getTimetables()
            ))
            .collect(Collectors.toList());

    }

    public GradeResponseDTO readById(UUID id) {

        Grade grade = gradeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found."));
        
        return new GradeResponseDTO(
            grade.getId(),
            grade.getSubject().getId(),
            grade.getProfessor().getId(),
            grade.getCapacity(),
            grade.getSemester(),
            grade.getLocate(),
            grade.getTimetables()
        );

    }

    public String update(UUID id, GradeUpdateDTO data) {

        Grade grade = gradeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
    
        data.status().ifPresent(grade::setStatus);

        grade.setCurrentStudents(grade.getEnrollees().size()); // Atualiza o Número de Estudantes.
        
        gradeR.save(grade);
        return "Updated" + entity;
        
    }

    @Transactional
    public String delete(UUID id) {

        if (!gradeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found.");
        
        enrolleeR.removeGradeFromEnrollees(id);
        gradeR.deleteById(id);
        return "Deleted" + entity;

    }

}
