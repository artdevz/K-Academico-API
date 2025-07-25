package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.exam.ExamRequestDTO;
import com.kacademico.app.dto.exam.ExamResponseDTO;
import com.kacademico.app.dto.exam.ExamUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Exam;
import com.kacademico.domain.repositories.IExamRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExamService {
    
    private final IExamRepository examR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(ExamRequestDTO data) {
        log.info("[API] Iniciando criação de avaliação para gradeId: {}", data.grade());
        
        Exam exam = requestMapper.toExam(data);
        Exam saved = examR.save(exam);
        
        log.info("[API] Avaliação criada com sucesso. ID: {}", saved.getId());
        return CompletableFuture.completedFuture("Exam successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<ExamResponseDTO>> readAllAsync() {
        log.debug("[API] Buscando todas as avaliações");
        List<ExamResponseDTO> response = responseMapper.toResponseDTOList(examR.findAll(), responseMapper::toExamResponseDTO);

        log.debug("[API] Encontradas {} avaliações", response.size());
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<ExamResponseDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscando avaliação com ID: {}", id);
        Exam exam = finder.findByIdOrThrow(examR.findById(id), "Exam not Found");

        log.debug("[API] Avaliação encontrada: {}", exam.getId());
        return CompletableFuture.completedFuture(responseMapper.toExamResponseDTO(exam));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, ExamUpdateDTO data) {
        log.info("[API] Atualizando avaliação com ID: {}", id);
        Exam exam = finder.findByIdOrThrow(examR.findById(id), "Exam not Found");
            
        examR.save(exam);
        log.info("[API] Avaliação atualizada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Exam");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.warn("[API] Solicitada exclusão da avaliação com ID: {}", id);
        finder.findByIdOrThrow(examR.findById(id), "Exam not Found");
        
        examR.deleteById(id);
        log.info("[API] Avaliação deletada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Deleted Exam");
    }

}