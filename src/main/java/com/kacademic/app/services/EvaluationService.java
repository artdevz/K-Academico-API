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

import com.kacademic.app.dto.evaluation.EvaluationRequestDTO;
import com.kacademic.app.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.app.dto.evaluation.EvaluationUpdateDTO;
import com.kacademic.domain.models.Enrollee;
import com.kacademic.domain.models.Evaluation;
import com.kacademic.domain.models.Exam;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.EvaluationRepository;
import com.kacademic.domain.repositories.ExamRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EvaluationService {
    
    private final EvaluationRepository evaluationR;
    private final EnrolleeRepository enrolleeR;
    private final ExamRepository examR;

    private final AsyncTaskExecutor taskExecutor;
    
    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(EvaluationRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Enrollee enrollee = findEnrolleeWithDetails(data.enrollee());
            Exam exam = findExamWithGrade(data.exam());

            ensureEvaluationNotExists(enrollee, exam);
            ensureSameGrade(enrollee, exam);

            Evaluation evaluation = new Evaluation(
                enrollee,
                exam,
                data.score()
            );
            
            evaluationR.save(evaluation);
            updateAverage(enrollee);
            return "Evaluation successfully Created: " + evaluation.getId();
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<EvaluationResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return (
                evaluationR.findAll().stream()
                .map(evaluation -> new EvaluationResponseDTO(
                    evaluation.getId(),
                    evaluation.getEnrollee().getId(),
                    evaluation.getExam().getGrade().getId(),
                    evaluation.getExam().getId(),
                    evaluation.getScore()
                ))
                .collect(Collectors.toList())
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<EvaluationResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Evaluation evaluation = evaluationR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation not Found"));
            
            return (
                new EvaluationResponseDTO(
                    evaluation.getId(),
                    evaluation.getEnrollee().getId(),
                    evaluation.getExam().getGrade().getId(),
                    evaluation.getExam().getId(),
                    evaluation.getScore()
                )
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, EvaluationUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Evaluation evaluation = evaluationR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation not Found"));
            
            data.score().ifPresent(evaluation::setScore);
            if (data.score().isPresent()) updateAverage(evaluation.getEnrollee());
                
            evaluationR.save(evaluation);
            return "Updated Evaluation";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Evaluation evaluation = evaluationR.findById(id) 
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation not Found"));
            
            evaluationR.deleteById(id);
            updateAverage(evaluation.getEnrollee());
            return "Deleted Evaluation";
        }, taskExecutor);
    }

    private Enrollee findEnrolleeWithDetails(UUID enrolleeId) {
        return enrolleeR.findByIdWithEvaluationsAndAttendances(enrolleeId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee Not Found"));
    }

    private Exam findExamWithGrade(UUID examId) {
        return examR.findByIdWithGrade(examId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found"));
    }

    private void ensureEvaluationNotExists(Enrollee enrollee, Exam exam) {
        if (evaluationR.existsByEnrolleeIdAndExamId(enrollee.getId(), exam.getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Evaluation already exists for this Enrollee and Exam");
    }

    private void ensureSameGrade(Enrollee enrollee, Exam exam) {
        if (!(enrollee.getGrade().getId().equals(exam.getGrade().getId())))
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enrollee and Exam must belong to the same Grade");
    }
    
    private void updateAverage(Enrollee enrollee) {
        enrollee.setAverage( (float)enrollee.getEvaluations().stream().mapToDouble(Evaluation::getScore).average().orElse(0.0));
        enrolleeR.save(enrollee);
    }

}