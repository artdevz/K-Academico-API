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

import com.kacademico.dtos.subject.SubjectRequestDTO;
import com.kacademico.dtos.subject.SubjectResponseDTO;
import com.kacademico.models.Subject;
import com.kacademico.repositories.SubjectRepository;

@Service
public class SubjectService {
    
    private final SubjectRepository subjectR;

    private final MappingService mapS;

    public SubjectService(SubjectRepository subjectR, MappingService mapS) {
        this.subjectR = subjectR;
        this.mapS = mapS;
    }    

    public void create(SubjectRequestDTO data) {        

        Subject subject = new Subject(
            mapS.findCourseById(data.course()),
            data.name(),
            data.description(),
            data.duration(),
            data.semester(),
            data.prerequisites()
        );
        
        subject.getCourse().setDuration(subject.getCourse().getDuration()+data.duration()); // Adiciona no Curso as Horas dessa Disciplina.
        subjectR.save(subject);

    }

    public List<SubjectResponseDTO> readAll() {

        return subjectR.findAll().stream()
            .map(subject -> new SubjectResponseDTO(
                subject.getId(),
                subject.getCourse().getName(),                
                subject.getName(),
                subject.getDescription(),
                subject.getDuration(),
                subject.getSemester(),
                subject.getPrerequisites()
            ))
            .collect(Collectors.toList());
    }

    public SubjectResponseDTO readById(UUID id) {

        Subject subject = subjectR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada."));
        
        return new SubjectResponseDTO(
            subject.getId(),
            subject.getCourse().getName(),                
            subject.getName(),
            subject.getDescription(),
            subject.getDuration(),
            subject.getSemester(),
            subject.getPrerequisites()
        );
    }

    public Subject update(UUID id, Map<String, Object> fields) {

        Optional<Subject> existingSubject = subjectR.findById(id);
    
        if (existingSubject.isPresent()) {
            Subject subject = existingSubject.get();            
    
            fields.forEach((key, value) -> {
                switch (key) {

                    case "name":
                        String name = (String) value;
                        subject.setName(name);
                        break;
                        
                    case "description":
                        String description = (String) value;
                        subject.setName(description);
                        break;         

                    default:
                        Field field = ReflectionUtils.findField(Subject.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, subject, value);
                        }
                        break;
                }
            });
            
            return subjectR.save(subject);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada.");
        
    }

    public void delete(UUID id) {

        if (!subjectR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina não encontrada.");
        subjectR.deleteById(id);

    }

}
