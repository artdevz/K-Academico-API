package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.equivalence.EquivalenceResponseDTO;
import com.kacademic.app.dto.subject.SubjectDetailsDTO;
import com.kacademic.app.dto.subject.SubjectRequestDTO;
import com.kacademic.app.dto.subject.SubjectResponseDTO;
import com.kacademic.app.dto.subject.SubjectUpdateDTO;
import com.kacademic.app.mapper.RequestMapper;
import com.kacademic.app.mapper.ResponseMapper;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Equivalence;
import com.kacademic.domain.models.Subject;
import com.kacademic.domain.repositories.CourseRepository;
import com.kacademic.domain.repositories.EquivalenceRepository;
import com.kacademic.domain.repositories.SubjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubjectService {
    
    private final SubjectRepository subjectR;
    private final CourseRepository courseR;
    private final EquivalenceRepository equivalenceR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;

    @Transactional
    @Async
    public CompletableFuture<String> createAsync(SubjectRequestDTO data) {
        Subject subject = requestMapper.toSubject(data);
        
        updateCourseDuration(courseR.findWithSubjectsById(data.course()).get(), data.duration());
        
        subjectR.save(subject);
        return CompletableFuture.completedFuture("Subject successfully Created: " + subject.getId());
    }

    @Async
    public CompletableFuture<List<SubjectResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toSubjectResponseDTOList(subjectR.findAll()));
    }

    @Async
    public CompletableFuture<SubjectDetailsDTO> readByIdAsync(UUID id) {
        Subject subject = findSubject(id);
        
        return CompletableFuture.completedFuture(
            new SubjectDetailsDTO(
                new SubjectResponseDTO(
                    subject.getId(),
                    subject.getCourse().getId(),                
                    subject.getName(),
                    subject.getDescription(),
                    subject.getDuration(),
                    subject.getSemester(),
                    subject.isRequired()
                ),
                subject.getPrerequisites().stream().map(prerequisites -> new EquivalenceResponseDTO(
                    prerequisites.getId(),
                    prerequisites.getName()
                )).collect(Collectors.toList())
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, SubjectUpdateDTO data) {
        Subject subject = findSubject(id);
            
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
        Subject subject = findSubject(id);

        updateCourseDuration(subject.getCourse(), subject.getDuration() * (-1)); // -1 for REMOVE

        subjectR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Subject");
    }

    private void updateCourseDuration(Course course, int duration) {
        course.setDuration(course.getDuration() + duration);
        courseR.save(course);
    }

    private Subject findSubject(UUID id) {
        return subjectR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject not Found"));
    }

    private Course findCourseDetails(UUID id) {
        return courseR.findWithSubjectsById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not Found"));
    }

    private List<Equivalence> findEquivalences(List<UUID> equivalences) {
        return equivalences.stream()
        .map(equivalenceId -> equivalenceR.findById(equivalenceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equivalence not Found")))
        .collect(Collectors.toList());
    }

}