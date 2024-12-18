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
import com.kacademico.enums.EGrade;
import com.kacademico.models.Grade;
import com.kacademico.repositories.EnrolleeRepository;
import com.kacademico.repositories.GradeRepository;
import com.kacademico.utils.Semester;

import jakarta.transaction.Transactional;

@Service
public class GradeService {
    
    private final GradeRepository gradeR;
    private final EnrolleeRepository enrolleeR;
    
    private final StudentService studentS;
    private final MappingService mapS;

    public GradeService(GradeRepository gradeR, EnrolleeRepository enrolleeR, StudentService studentS, MappingService mapS) {
        this.gradeR = gradeR;
        this.enrolleeR = enrolleeR;
        this.studentS = studentS;
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
                grade.getProfessor().getUser().getName(),
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
            grade.getProfessor().getUser().getName(),
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

    public void finish(@Semester String semester) {
     
        List<Grade> grades = gradeR.findAll();

        for (Grade grade : grades) if (grade.getStatus().equals(EGrade.ONGOING) && grade.getSemester().equals(semester)) grade.setStatus(EGrade.FINISHED);
        
        studentS.updateStatus();
        studentS.updateAvarage();

    }

}
