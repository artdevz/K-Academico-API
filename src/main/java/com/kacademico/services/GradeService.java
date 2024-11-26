package com.kacademico.services;

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

import com.kacademico.dtos.grade.GradeRequestDTO;
import com.kacademico.dtos.grade.GradeResponseDTO;
import com.kacademico.models.Grade;
import com.kacademico.repositories.GradeRepository;

@Service
public class GradeService {
    
    private final GradeRepository gradeR;
    
    private final MappingService mapS;

    public GradeService(GradeRepository gradeR, MappingService mapS) {
        this.gradeR = gradeR;
        this.mapS = mapS;
    }
    
    public void create(GradeRequestDTO data) {

        Grade grade = new Grade(
            mapS.findSubjectById(data.subject()),
            mapS.findProfessorById(data.professor()),
            data.capacity(),
            data.timetable(),
            data.locate()
        );

        gradeR.save(grade);

    }

    public List<GradeResponseDTO> readAll() {

        return gradeR.findAll().stream()
            .map(grade -> new GradeResponseDTO(
                grade.getId(),
                grade.getSubject().getName(),
                grade.getProfessor().getUser().getName(),
                grade.getCapacity(),
                grade.getTimetables(),
                grade.getLocate()
            ))
            .collect(Collectors.toList());

    }

    public GradeResponseDTO readById(UUID id) {

        Grade grade = gradeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada."));
        
        return new GradeResponseDTO(
            grade.getId(),
            grade.getSubject().getName(),
            grade.getProfessor().getUser().getName(),
            grade.getCapacity(),
            grade.getTimetables(),
            grade.getLocate()
        );

    }

    public Grade update(UUID id, Map<String, Object> fields) {

        Optional<Grade> existingGrade = gradeR.findById(id);
    
        if (existingGrade.isPresent()) {
            Grade grade = existingGrade.get();            
    
            fields.forEach((key, value) -> {
                switch (key) {                                   

                    default:
                        Field field = ReflectionUtils.findField(Grade.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, grade, value);
                        }
                        break;
                }
            });
            
            return gradeR.save(grade);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada.");
        
    }

    public void delete(UUID id) {

        if (!gradeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Turma não encontrada.");
            gradeR.deleteById(id);

    }

}
