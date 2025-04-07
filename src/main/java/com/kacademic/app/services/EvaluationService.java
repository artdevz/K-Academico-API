package com.kacademic.app.services;

import java.util.List;
import java.util.Set;
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
            Enrollee enrollee = enrolleeR.findByIdWithEvaluationsAndAttendances(data.enrollee())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
            
            Exam exam = examR.findByIdWithGrade(data.exam())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found"));

            if (!belongsToSameGrade(enrollee, exam)) throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enrollee and Exam must belong to the same Grade");
            if (evaluationR.existsByEnrolleeIdAndExamId(enrollee.getId(), exam.getId())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Evaluation already exists for this Enrollee and Exam");

            Evaluation evaluation = new Evaluation(
                enrollee,
                exam,
                data.score()
            );
            
            addEvaluation(evaluation);
            evaluationR.save(evaluation);
            return "Created Evaluation";
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
            if (data.score().isPresent()) editEvaluation(evaluation);
                
            evaluationR.save(evaluation);
            return "Updated Evaluation";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            if (!evaluationR.findById(id).isPresent()) 
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation not Found");
            
            removeEvaluation(evaluationR.findById(id).get());
            
            evaluationR.deleteById(id);
            return "Deleted Evaluation";
        }, taskExecutor);
    }

    private void addEvaluation(Evaluation evaluation) {
        Enrollee enrollee = enrolleeR.findByIdWithEvaluationsAndAttendances(evaluation.getEnrollee().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
        
        enrollee.getEvaluations().add(evaluation);
        enrollee.setAverage(updateAverage(enrollee.getEvaluations()));

        enrolleeR.save(enrollee);
    }

    private void editEvaluation(Evaluation evaluation) {
        evaluation.getEnrollee().setAverage(updateAverage(evaluation.getEnrollee().getEvaluations()));
    }    

    private void removeEvaluation(Evaluation evaluation) {
        Enrollee enrollee = enrolleeR.findByIdWithEvaluationsAndAttendances(evaluation.getEnrollee().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));

        enrollee.getEvaluations().removeIf(currentEvaluation -> currentEvaluation.equals(evaluation));

        enrollee.setAverage(updateAverage(enrollee.getEvaluations()));

        enrolleeR.save(enrollee);
    }

    private float updateAverage(Set<Evaluation> evaluations) {
        float sum = 0;
        for (Evaluation evaluation : evaluations) sum += evaluation.getScore();

        return evaluations.isEmpty() ? 0 : sum / evaluations.size();
    }

    private boolean belongsToSameGrade(Enrollee enrollee, Exam exam) {
        return enrollee.getGrade().getId().equals(exam.getGrade().getId()); 
    }

}