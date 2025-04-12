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

import com.kacademic.app.dto.lesson.LessonRequestDTO;
import com.kacademic.app.dto.lesson.LessonResponseDTO;
import com.kacademic.app.dto.lesson.LessonUpdateDTO;
import com.kacademic.domain.models.Grade;
import com.kacademic.domain.models.Lesson;
import com.kacademic.domain.repositories.GradeRepository;
import com.kacademic.domain.repositories.LessonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LessonService {
    
    private final LessonRepository lessonR;
    private final GradeRepository gradeR;

    private final AsyncTaskExecutor taskExecutor;

    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(LessonRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Lesson lesson = new Lesson(
                findGrade(data.grade()),
                data.topic(),
                data.date()
            );
            
            lessonR.save(lesson);
            return "Lesson successfully Created: " + lesson.getId();
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<LessonResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return lessonR.findAll().stream()
                .map(lesson -> new LessonResponseDTO(
                    lesson.getId(),
                    lesson.getGrade().getId(),
                    lesson.getTopic(),
                    lesson.getDate(),
                    lesson.getStatus()
                ))
                .collect(Collectors.toList()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<LessonResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Lesson lesson = findLesson(id);
    
            return new LessonResponseDTO(
                lesson.getId(),
                lesson.getGrade().getId(),
                lesson.getTopic(),
                lesson.getDate(),
                lesson.getStatus()
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, LessonUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Lesson lesson = findLesson(id);
    
            lessonR.save(lesson);
            return "Updated Lesson";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            findLesson(id);
            
            lessonR.deleteById(id);
            return "Deleted Lesson";
        }, taskExecutor);
    }

    private Lesson findLesson(UUID id) {
        return lessonR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found"));
    }
    
    private Grade findGrade(UUID id) {
        return gradeR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found"));
    }

}