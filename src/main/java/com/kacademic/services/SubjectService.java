package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.subject.SubjectRequestDTO;
import com.kacademic.dto.subject.SubjectResponseDTO;
import com.kacademic.dto.subject.SubjectUpdateDTO;
import com.kacademic.models.Subject;
import com.kacademic.repositories.CourseRepository;
import com.kacademic.repositories.SubjectRepository;

@Service
public class SubjectService {
    
    private final SubjectRepository subjectR;
    private final CourseRepository courseR;

    private final String entity = "Subject";

    public SubjectService(SubjectRepository subjectR, CourseRepository courseR) {
        this.subjectR = subjectR;
        this.courseR = courseR;
    }    

    public String create(SubjectRequestDTO data) {        

        Subject subject = new Subject(
            courseR.findById(data.course()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found")),
            data.name(),
            data.description(),
            data.duration(),
            data.semester(),
            data.prerequisites()
        );
        
        subject.getCourse().setDuration(subject.getCourse().getDuration()+data.duration()); // Adiciona no Curso as Horas dessa Disciplina.
        subjectR.save(subject);
        return "Created " + entity;

    }

    public List<SubjectResponseDTO> readAll() {

        return subjectR.findAll().stream()
            .map(subject -> new SubjectResponseDTO(
                subject.getId(),
                subject.getCourse().getId(),                
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found."));
        
        return new SubjectResponseDTO(
            subject.getId(),
            subject.getCourse().getId(),                
            subject.getName(),
            subject.getDescription(),
            subject.getDuration(),
            subject.getSemester(),
            subject.getPrerequisites()
        );
    }

    public String update(UUID id, SubjectUpdateDTO data) {

        Subject subject = subjectR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found."));
            
        data.type().ifPresent(subject::setType);
        data.name().ifPresent(subject::setName);
        data.description().ifPresent(subject::setDescription);
        data.duration().ifPresent(subject::setDuration);
        data.semester().ifPresent(subject::setSemester);

        subjectR.save(subject);
        return "Updated " + entity;
                
    }

    public String delete(UUID id) {

        if (!subjectR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not found.");

        subjectR.findById(id).get().getCourse().setDuration(        // Remove no Curso as Horas dessa Disciplina.
            subjectR.findById(id).get().getCourse().getDuration() - // Duração do Curso
            subjectR.findById(id).get().getDuration() );            // Duração da Disciplina

        subjectR.deleteById(id);
        return "Deleted " + entity;

    }

}
