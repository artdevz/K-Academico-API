package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.lesson.LessonRequestDTO;
import com.kacademico.app.dto.lesson.LessonResponseDTO;
import com.kacademico.app.dto.lesson.LessonUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Lesson;
import com.kacademico.domain.repositories.LessonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LessonService {
    
    private final LessonRepository lessonR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(LessonRequestDTO data) {
        Lesson lesson = requestMapper.toLesson(data);
        
        lessonR.save(lesson);
        return CompletableFuture.completedFuture("Lesson successfully Created: " + lesson.getId());
    }

    @Async
    public CompletableFuture<List<LessonResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(lessonR.findAll(), responseMapper::toLessonResponseDTO));
    }

    @Async
    public CompletableFuture<LessonResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.completedFuture(responseMapper.toLessonResponseDTO(finder.findByIdOrThrow(lessonR.findById(id), "Lesson not Found")));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, LessonUpdateDTO data) {
        Lesson lesson = finder.findByIdOrThrow(lessonR.findById(id), "Lesson not Found");

        lessonR.save(lesson);
        return CompletableFuture.completedFuture("Updated Lesson");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(lessonR.findById(id), "Lesson not Found");
        
        lessonR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Lesson");
    }

}