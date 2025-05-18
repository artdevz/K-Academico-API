package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.app.dto.evaluation.EvaluationRequestDTO;
import com.kacademico.app.dto.evaluation.EvaluationResponseDTO;
import com.kacademico.app.dto.evaluation.EvaluationUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.models.Evaluation;
import com.kacademico.domain.models.Exam;
import com.kacademico.domain.repositories.IEnrolleeRepository;
import com.kacademico.domain.repositories.IEvaluationRepository;
import com.kacademico.domain.repositories.IExamRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EvaluationService {
    
    private final IEvaluationRepository evaluationR;
    private final IEnrolleeRepository enrolleeR;
    private final IExamRepository examR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;
    
    @Async
    public CompletableFuture<String> createAsync(EvaluationRequestDTO data) {
        Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findByIdWithEvaluationsAndAttendances(data.enrollee()), "Enrollee not Found");
        Exam exam = finder.findByIdOrThrow(examR.findByIdWithGrade(data.exam()), "Exam not Found");

        ensureEvaluationNotExists(enrollee, exam);
        ensureSameGrade(enrollee, exam);

        Evaluation evaluation = requestMapper.toEvaluation(data);
        
        evaluationR.save(evaluation);
        updateAverage(enrollee);
        return CompletableFuture.completedFuture("Evaluation successfully Created: " + evaluation.getId());
    }

    @Async
    public CompletableFuture<List<EvaluationResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(evaluationR.findAll(), responseMapper::toEvaluationResponseDTO));
    }

    @Async
    public CompletableFuture<EvaluationResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.completedFuture(responseMapper.toEvaluationResponseDTO(finder.findByIdOrThrow(evaluationR.findById(id), "Evaluation not Found")));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, EvaluationUpdateDTO data) {
        Evaluation evaluation = finder.findByIdOrThrow(evaluationR.findById(id), "Evaluation not Found");
        
        data.score().ifPresent(evaluation::setScore);
        if (data.score().isPresent()) updateAverage(evaluation.getEnrollee());
            
        evaluationR.save(evaluation);
        return CompletableFuture.completedFuture("Updated Evaluation");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        Evaluation evaluation = finder.findByIdOrThrow(evaluationR.findById(id), "Evaluation not Found");
        
        evaluationR.deleteById(id);
        updateAverage(evaluation.getEnrollee());
        return CompletableFuture.completedFuture("Updated Evaluation");
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