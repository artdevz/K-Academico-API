package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kacademico.app.dto.equivalence.EquivalenceResponseDTO;
import com.kacademico.app.dto.subject.SubjectDetailsDTO;
import com.kacademico.app.dto.subject.SubjectRequestDTO;
import com.kacademico.app.dto.subject.SubjectResponseDTO;
import com.kacademico.app.dto.subject.SubjectUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Course;
import com.kacademico.domain.models.Subject;
import com.kacademico.domain.repositories.ICourseRepository;
import com.kacademico.domain.repositories.ISubjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubjectService {
    
    private final ISubjectRepository subjectR;
    private final ICourseRepository courseR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Transactional
    @Async
    public CompletableFuture<String> createAsync(SubjectRequestDTO data) {
        Subject subject = requestMapper.toSubject(data);
        
        Subject saved = subjectR.save(subject);
        updateCourseDuration(subject.getCourse());

        return CompletableFuture.completedFuture("Subject successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<SubjectResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(subjectR.findAll(), responseMapper::toSubjectResponseDTO));
    }

    @Async
    public CompletableFuture<SubjectDetailsDTO> readByIdAsync(UUID id) {
        Subject subject = finder.findByIdOrThrow(subjectR.findById(id), "Subject not Found");
        return CompletableFuture.completedFuture(
            new SubjectDetailsDTO(
                responseMapper.toSubjectResponseDTO(subject),
                subject.getPrerequisites().stream().map(prerequisites -> new EquivalenceResponseDTO(
                    prerequisites.getId(),
                    prerequisites.getName()
                )).collect(Collectors.toList())
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, SubjectUpdateDTO data) {
        Subject subject = finder.findByIdOrThrow(subjectR.findById(id), "Subject not Found");
            
        data.isRequired().ifPresent(subject::setRequired);
        data.name().ifPresent(subject::setName);
        data.description().ifPresent(subject::setDescription);
        // data.duration().ifPresent(subject::setDuration);
        // data.semester().ifPresent(subject::setSemester);

        subjectR.save(subject);
        return CompletableFuture.completedFuture("Updated Subject");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        Subject subject = finder.findByIdOrThrow(subjectR.findById(id), "Subject not Found");

        subjectR.deleteById(id);
        updateCourseDuration(subject.getCourse());

        return CompletableFuture.completedFuture("Deleted Subject");
    }

    private void updateCourseDuration(Course course) {
        System.out.println("Attualizando");
        int duration = course.getSubjects().stream()
            .mapToInt(Subject::getDuration).sum();
        course.setDuration(duration);
        courseR.save(course);
        System.out.println("Atualizado");
    }

}