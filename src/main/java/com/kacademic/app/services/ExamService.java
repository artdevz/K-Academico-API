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

import com.kacademic.app.dto.exam.ExamRequestDTO;
import com.kacademic.app.dto.exam.ExamResponseDTO;
import com.kacademic.app.dto.exam.ExamUpdateDTO;
import com.kacademic.domain.models.Exam;
import com.kacademic.domain.models.Grade;
import com.kacademic.domain.repositories.ExamRepository;
import com.kacademic.domain.repositories.GradeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ExamService {
    
    private final ExamRepository examR;
    private final GradeRepository gradeR;

    private final AsyncTaskExecutor taskExecutor;

    @Transactional
    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(ExamRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Exam exam = new Exam(
                data.name(),
                data.maximum(),
                data.date(),
                findGrade(data.grade())
            );
    
            examR.save(exam);
            return "Exam successfully Created: " + exam.getId();
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<ExamResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return (
                examR.findAll().stream()
                .map(exam -> new ExamResponseDTO(
                    exam.getId(),
                    exam.getGrade().getId(),
                    exam.getName(),                
                    exam.getMaximum(),
                    exam.getDate()
                ))
                .collect(Collectors.toList())
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<ExamResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Exam exam = findExam(id);
            
            return (
                new ExamResponseDTO(
                    exam.getId(),
                    exam.getGrade().getId(),
                    exam.getName(),            
                    exam.getMaximum(),
                    exam.getDate()
                )
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, ExamUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Exam exam = findExam(id);
                
            examR.save(exam);
            return "Updated Exam";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            findExam(id);
            
            examR.deleteById(id);
            return "Deleted Exam";
        }, taskExecutor);
    }

    private Exam findExam(UUID id) {
        return examR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found"));
    }

    private Grade findGrade(UUID id) {
        return gradeR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found"));
    }

}