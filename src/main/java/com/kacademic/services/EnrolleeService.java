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

import com.kacademic.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.dto.enrollee.EnrolleeResponseDTO;
import com.kacademic.models.Enrollee;
import com.kacademic.repositories.EnrolleeRepository;

@Service
public class EnrolleeService {
    
    private final EnrolleeRepository enrolleeR;
    
    private final MappingService mapS;

    public EnrolleeService(EnrolleeRepository enrolleeR, MappingService mapS) {
        this.enrolleeR = enrolleeR;
        this.mapS = mapS;
    }
    
    public void create(EnrolleeRequestDTO data) {

        Enrollee enrollee = new Enrollee(
            mapS.findStudentById(data.student()),
            mapS.findStudentById(data.student()).getTranscript(),
            mapS.findGradeById(data.grade())
        );

        enrollee.getGrade().setCurrentStudents( // Adiciona 1 Estudante na Turma
            enrollee.getGrade().getCurrentStudents() + 1 
        );
        enrolleeR.save(enrollee);

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

    public EnrolleeResponseDTO readById(UUID id) {

        Enrollee enrollee = enrolleeR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found."));
        
        return new EnrolleeResponseDTO(
            enrollee.getId(),
            enrollee.getStudent().getId(),
            enrollee.getGrade().getId(),
            enrollee.getAbsences(),
            enrollee.getAvarage(),
            enrollee.getStatus()
        );

    }

    public Enrollee update(UUID id, Map<String, Object> fields) {

        Optional<Enrollee> existingEnrollee = enrolleeR.findById(id);
    
        if (existingEnrollee.isPresent()) {
            Enrollee enrollee = existingEnrollee.get();            
    
            fields.forEach((key, value) -> {
                switch (key) {                                   

                    default:
                        Field field = ReflectionUtils.findField(Enrollee.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, enrollee, value);
                        }
                        break;
                }
            });
            
            return enrolleeR.save(enrollee);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found.");
        
    }

    public void delete(UUID id) {

        if (!enrolleeR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found.");

        enrolleeR.findById(id).get().getGrade().setCurrentStudents( // Remove 1 Estudante na Turma
            enrolleeR.findById(id).get().getGrade().getCurrentStudents() - 1
        );
        enrolleeR.deleteById(id);

    }

}
