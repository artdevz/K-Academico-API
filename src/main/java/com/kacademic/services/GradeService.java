package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.grade.GradeRequestDTO;
import com.kacademic.dto.grade.GradeResponseDTO;
import com.kacademic.enums.EGrade;
import com.kacademic.models.Grade;
import com.kacademic.repositories.EnrolleeRepository;
import com.kacademic.repositories.GradeRepository;

import jakarta.transaction.Transactional;

@Service
public class GradeService {
    
    private final GradeRepository gradeR;
    private final EnrolleeRepository enrolleeR;
    
    private final MappingService mapS;

    public GradeService(GradeRepository gradeR, EnrolleeRepository enrolleeR, MappingService mapS) {
        this.gradeR = gradeR;
        this.enrolleeR = enrolleeR;
        this.mapS = mapS;
    }
    
    public void create(GradeRequestDTO data) {

        Grade grade = new Grade(
            mapS.findSubjectById(data.subject()),
            mapS.findProfessorById(data.professor()),
            data.capacity(),
            data.semester(),
            data.locate(),
            data.timetable()
        );

        gradeR.save(grade);

    }

    public List<GradeResponseDTO> readAll() {

        return gradeR.findAll().stream()
            .map(grade -> new GradeResponseDTO(
                grade.getId(),
                grade.getSubject().getName(),
                grade.getProfessor().getName(),
                grade.getCapacity(),
                grade.getSemester(),
                grade.getLocate(),
                grade.getTimetables()
            ))
            .collect(Collectors.toList());

    }

    public GradeResponseDTO readById(UUID id) {

        Grade grade = gradeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada."));
        
        return new GradeResponseDTO(
            grade.getId(),
            grade.getSubject().getName(),
            grade.getProfessor().getName(),
            grade.getCapacity(),
            grade.getSemester(),
            grade.getLocate(),
            grade.getTimetables()
        );

    }

    public Grade update(UUID id, Map<String, Object> fields) {

        Optional<Grade> existingGrade = gradeR.findById(id);
    
        if (existingGrade.isPresent()) {
            Grade grade = existingGrade.get();            
    
            fields.forEach((key, value) -> {
                switch (key) {   
                    
                    case "status":
                        EGrade status = (EGrade) value;
                        grade.setStatus(status);
                        break;

                    default:
                        Field field = ReflectionUtils.findField(Grade.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, grade, value);
                        }
                        break;
                }
            });

            grade.setNumberOfStudents(grade.getEnrollees().size()); // Atualiza o Número de Estudantes.
            
            return gradeR.save(grade);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada.");
        
    }

    @Transactional
    public void delete(UUID id) {

        if (!gradeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada.");
        
        enrolleeR.removeGradeFromEnrollees(id);
        gradeR.deleteById(id);

    }

}
