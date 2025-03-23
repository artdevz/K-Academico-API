package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.lesson.LessonRequestDTO;
import com.kacademic.dto.lesson.LessonResponseDTO;
import com.kacademic.dto.lesson.LessonUpdateDTO;
import com.kacademic.models.Lesson;
import com.kacademic.repositories.GradeRepository;
import com.kacademic.repositories.LessonRepository;

@Service
public class LessonService {
    
    private final LessonRepository lessonR;
    private final GradeRepository gradeR;

    private final String entity = "Lesson";

    public LessonService(LessonRepository lessonR, GradeRepository gradeR) {
        this.lessonR = lessonR;
        this.gradeR = gradeR;
    }

    @Async
    public CompletableFuture<String> createAsync(LessonRequestDTO data) {

        Lesson lesson = new Lesson(
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found.")),
            data.topic(),
            data.date()
        );

        lessonR.save(lesson);
        return CompletableFuture.completedFuture("Created " + entity);
        
    }

    @Async
    public CompletableFuture<List<LessonResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
            lessonR.findAll().stream()
            .map(lesson -> new LessonResponseDTO(
                lesson.getId(),
                lesson.getGrade().getId(),
                lesson.getTopic(),                
                lesson.getDate(),
                lesson.getStatus()
            ))
            .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<LessonResponseDTO> readByIdAsync(UUID id) {

        Lesson lesson = lessonR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
        
        return CompletableFuture.completedFuture(
            new LessonResponseDTO(
                lesson.getId(),
                lesson.getGrade().getId(),
                lesson.getTopic(),                
                lesson.getDate(),
                lesson.getStatus()
        ));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, LessonUpdateDTO data) {

        Lesson lesson = lessonR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));

        lessonR.save(lesson);
        return CompletableFuture.completedFuture("Updated " + entity);
        
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!lessonR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found.");
        
        lessonR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted " + entity);

    }

}
