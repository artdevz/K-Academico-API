package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.subject.SubjectRequestDTO;
import com.kacademic.app.dto.subject.SubjectResponseDTO;
import com.kacademic.app.dto.subject.SubjectUpdateDTO;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Subject;
import com.kacademic.domain.repositories.CourseRepository;
import com.kacademic.domain.repositories.SubjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubjectService {
    
    private final SubjectRepository subjectR;
    private final CourseRepository courseR;

    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(SubjectRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Subject subject = new Subject(
                courseR.findByIdWithSubjects(data.course()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found")),
                data.name(),
                data.description(),
                data.duration(),
                data.semester(),
                data.prerequisites()
            );
            
            updateCourseDuration(courseR.findByIdWithSubjects(data.course()).get(), data.duration());
            
            subjectR.save(subject);
            return "Created Subject";
        }, taskExecutor);        
    }

    @Async("taskExecutor")
    public CompletableFuture<List<SubjectResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
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
                .collect(Collectors.toList()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<SubjectResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Subject subject = subjectR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not Found."));
            
            return new SubjectResponseDTO(
                subject.getId(),
                subject.getCourse().getId(),                
                subject.getName(),
                subject.getDescription(),
                subject.getDuration(),
                subject.getSemester(),
                subject.getPrerequisites()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, SubjectUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Subject subject = subjectR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not Found"));
                
            data.type().ifPresent(subject::setType);
            data.name().ifPresent(subject::setName);
            data.description().ifPresent(subject::setDescription);
            // data.duration().ifPresent(subject::setDuration);
            // data.semester().ifPresent(subject::setSemester);
    
            subjectR.save(subject);
            return "Updated Subject";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Subject subject = subjectR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not Found"));
    
            updateCourseDuration(subject.getCourse(), subject.getDuration() * (-1)); // -1 for REMOVE
    
            subjectR.deleteById(id);
            return "Deleted Subject";
        }, taskExecutor);
    }

    private void updateCourseDuration(Course course, int duration) {
        course.setDuration(course.getDuration() + duration);
        courseR.save(course);
    }

}