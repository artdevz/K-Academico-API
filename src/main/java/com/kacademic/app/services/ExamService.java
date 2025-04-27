package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademic.app.dto.exam.ExamRequestDTO;
import com.kacademic.app.dto.exam.ExamResponseDTO;
import com.kacademic.app.dto.exam.ExamUpdateDTO;
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.app.mapper.RequestMapper;
import com.kacademic.app.mapper.ResponseMapper;
import com.kacademic.domain.models.Exam;
import com.kacademic.domain.repositories.ExamRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExamService {
    
    private final ExamRepository examR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    private final AsyncTaskExecutor taskExecutor;

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
        return CompletableFuture.supplyAsync(() -> {
            Exam exam = finder.findByIdOrThrow(examR.findById(id), "Exam not Found");
                
            examR.save(exam);
            return "Updated Exam";
        }, taskExecutor);
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            finder.findByIdOrThrow(examR.findById(id), "Exam not Found");
            
            examR.deleteById(id);
            return "Deleted Exam";
        }, taskExecutor);
    }

}