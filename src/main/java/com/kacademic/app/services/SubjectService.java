package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.subject.SubjectRequestDTO;
import com.kacademic.app.dto.subject.SubjectResponseDTO;
import com.kacademic.app.dto.subject.SubjectUpdateDTO;
import com.kacademic.domain.models.Subject;
import com.kacademic.domain.repositories.CourseRepository;
import com.kacademic.domain.repositories.SubjectRepository;

@Service
public class SubjectService {
    
    private final SubjectRepository subjectR;
    private final CourseRepository courseR;

    private final String entity = "Subject";

    public SubjectService(SubjectRepository subjectR, CourseRepository courseR) {
        this.subjectR = subjectR;
        this.courseR = courseR;
    }    

    @Async
    public CompletableFuture<String> createAsync(SubjectRequestDTO data) {        

        Subject subject = new Subject(
            courseR.findById(data.course()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found")),
            data.name(),
            data.description(),
            data.duration(),
            data.semester(),
            data.prerequisites()
        );
        
        subject.getCourse().setDuration(subject.getCourse().getDuration()+data.duration()); // Adiciona no Curso as Horas dessa Disciplina.
        subjectR.save(subject);
        return CompletableFuture.completedFuture("Created " + entity);

    }

    @Async
    public CompletableFuture<List<SubjectResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
            subjectR.findAll().stream()
            .map(subject -> new SubjectResponseDTO(
                subject.getId(),
                subject.getCourse().getId(),                
                subject.getName(),
                subject.getDescription(),
                subject.getDuration(),
                subject.getSemester(),
                subject.getPrerequisites()
            ))
            .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<SubjectResponseDTO> readByIdAsync(UUID id) {

        Subject subject = subjectR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
        
        return CompletableFuture.completedFuture(
            new SubjectResponseDTO(
                subject.getId(),
                subject.getCourse().getId(),                
                subject.getName(),
                subject.getDescription(),
                subject.getDuration(),
                subject.getSemester(),
                subject.getPrerequisites()
        ));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, SubjectUpdateDTO data) {

        Subject subject = subjectR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
            
        data.type().ifPresent(subject::setType);
        data.name().ifPresent(subject::setName);
        data.description().ifPresent(subject::setDescription);
        data.duration().ifPresent(subject::setDuration);
        data.semester().ifPresent(subject::setSemester);

        subjectR.save(subject);
        return CompletableFuture.completedFuture("Updated " + entity);
                
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!subjectR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found");

        subjectR.findById(id).get().getCourse().setDuration(        // Remove no Curso as Horas dessa Disciplina.
            subjectR.findById(id).get().getCourse().getDuration() - // Duração do Curso
            subjectR.findById(id).get().getDuration() );            // Duração da Disciplina

        subjectR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted " + entity);

    }

}
