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
import com.kacademico.domain.repositories.ExamRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExamService {
    
    private final ExamRepository examR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(ExamRequestDTO data) {
        Exam exam = requestMapper.toExam(data);

        examR.save(exam);
        return CompletableFuture.completedFuture("Exam successfully Created: " + exam.getId());
    }

    @Async
    public CompletableFuture<List<ExamResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(examR.findAll(), responseMapper::toExamResponseDTO));
    }

    @Async
    public CompletableFuture<ExamResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.completedFuture(responseMapper.toExamResponseDTO(finder.findByIdOrThrow(examR.findById(id), "Exam not Found")));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, ExamUpdateDTO data) {
        Exam exam = finder.findByIdOrThrow(examR.findById(id), "Exam not Found");
            
        examR.save(exam);
        return CompletableFuture.completedFuture("Updated Exam");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(examR.findById(id), "Exam not Found");
        
        examR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Exam");
    }

}