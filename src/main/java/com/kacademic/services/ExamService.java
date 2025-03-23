package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.exam.ExamRequestDTO;
import com.kacademic.dto.exam.ExamResponseDTO;
import com.kacademic.dto.exam.ExamUpdateDTO;
import com.kacademic.models.Exam;
import com.kacademic.repositories.ExamRepository;
import com.kacademic.repositories.GradeRepository;

@Service
public class ExamService {
    
    private final ExamRepository examR;
    private final GradeRepository gradeR;

    private final String entity = "Exam";

    public ExamService(ExamRepository examR, GradeRepository gradeR) {
        this.examR = examR;
        this.gradeR = gradeR;
    }

    @Async
    public CompletableFuture<String> createAsync(ExamRequestDTO data) {

        Exam exam = new Exam(
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found")),
            data.name(),
            data.maximum(),
            data.date()
        );

        examR.save(exam);
        return CompletableFuture.completedFuture("Created " + entity);
        
    }

    @Async
    public CompletableFuture<List<ExamResponseDTO>> readAllAsync() {

        return CompletableFuture.completedFuture(
            examR.findAll().stream()
            .map(exam -> new ExamResponseDTO(
                exam.getId(),
                exam.getGrade().getId(),
                exam.getName(),                
                exam.getMaximum(),
                exam.getDate()
            ))
            .collect(Collectors.toList()));
    }

    @Async
    public CompletableFuture<ExamResponseDTO> readByIdAsync(UUID id) {

        Exam exam = examR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found"));
        
        return CompletableFuture.completedFuture(
            new ExamResponseDTO(
                exam.getId(),
                exam.getGrade().getId(),
                exam.getName(),            
                exam.getMaximum(),
                exam.getDate()
        ));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, ExamUpdateDTO data) {

        Exam exam = examR.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
            
        examR.save(exam);
        return CompletableFuture.completedFuture("Updated " + entity);
                
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {

        if (!examR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found.");
        
        examR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted " + entity);

    }

}
