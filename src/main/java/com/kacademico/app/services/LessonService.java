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
import com.kacademico.domain.repositories.ILessonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LessonService {
    
    private final ILessonRepository lessonR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(LessonRequestDTO data) {
        log.info("[API] Iniciando a criação de aula para gradeId: {}", data.grade());

        Lesson lesson = requestMapper.toLesson(data);
        Lesson saved = lessonR.save(lesson);
        
        log.info("[API] Aula criada com sucesso. ID: {}", saved.getId());
        return CompletableFuture.completedFuture("Lesson successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<LessonResponseDTO>> readAllAsync() {
        log.debug("[API] Buscando todas as aulas");
        List<LessonResponseDTO> response = responseMapper.toResponseDTOList(lessonR.findAll(), responseMapper::toLessonResponseDTO);

        log.debug("[API] Encontradas {} aulas", response.size());
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<LessonResponseDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscando aula com ID: {}", id);
        Lesson lesson = finder.findByIdOrThrow(lessonR.findById(id), "Lesson not Found");

        log.debug("[API] Aula encontrada: {}", lesson.getId());
        return CompletableFuture.completedFuture(responseMapper.toLessonResponseDTO(lesson));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, LessonUpdateDTO data) {
        log.info("[API] Atualizando aula com ID: {}", id);
        Lesson lesson = finder.findByIdOrThrow(lessonR.findById(id), "Lesson not Found");

        lessonR.save(lesson);
        log.info("[API] Aula atualizada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Lesson");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.info("[API] Solicitada exclusão da aula com ID: {}", id);
        finder.findByIdOrThrow(lessonR.findById(id), "Lesson not Found");
        
        lessonR.deleteById(id);
        log.info("[API] Aula deletada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Deleted Lesson");
    }

}