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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("[API] Iniciando criação de Resultado para enrolleeId: {} e examId: {}", data.enrollee(), data.exam());
        
        Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findWithEvaluationsAndAttendancesById(data.enrollee()), "Enrollee not Found");
        Exam exam = finder.findByIdOrThrow(examR.findWithGradeById(data.exam()), "Exam not Found");

        ensureEvaluationNotExists(enrollee, exam);

        Evaluation evaluation = requestMapper.toEvaluation(data);
        Evaluation saved = evaluationR.save(evaluation);
        updateAverage(enrollee);
        
        log.info("[API] Resultado criado com sucesso. ID: {}", saved.getId());
        return CompletableFuture.completedFuture("Evaluation successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<EvaluationResponseDTO>> readAllAsync() {
        log.debug("[API] Buscando todos os resultados");
        List<EvaluationResponseDTO> response = responseMapper.toResponseDTOList(evaluationR.findAll(), responseMapper::toEvaluationResponseDTO);

        log.debug("[API] Encontrados {} resultados", response.size());
        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<EvaluationResponseDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscando resultado com ID: {}", id);
        Evaluation response = finder.findByIdOrThrow(evaluationR.findById(id), "Evaluation not Found");

        log.debug("[API] Resultado encontrado: {}", response.getId());
        return CompletableFuture.completedFuture(responseMapper.toEvaluationResponseDTO(response));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, EvaluationUpdateDTO data) {
        log.info("[API] Atualizando resultado com ID: {}", id);
        Evaluation evaluation = finder.findByIdOrThrow(evaluationR.findById(id), "Evaluation not Found");
        
        data.score().ifPresent(score -> {
            log.debug("[API] Atualizando campo 'score' para: {}", score);
            evaluation.setScore(score);
            updateAverage(evaluation.getEnrollee());
        });
            
        evaluationR.save(evaluation);
        log.info("[API] Resultado atualizado com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Evaluation");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.warn("[API] Solicitada exclusão de resultado com ID: {}", id);
        Evaluation evaluation = finder.findByIdOrThrow(evaluationR.findById(id), "Evaluation not Found");
        
        evaluationR.deleteById(id);
        updateAverage(evaluation.getEnrollee());

        log.info("[API] Resultado deletado com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Evaluation");
    }

    private void ensureEvaluationNotExists(Enrollee enrollee, Exam exam) {
        if (evaluationR.existsByEnrolleeIdAndExamId(enrollee.getId(), exam.getId())) {
            log.warn("[API] Tentativa de criar resultado duplicado para enrolleeId: {} e examId: {}", enrollee.getId(), exam.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Evaluation already exists for this Enrollee and Exam");
        }
    }
    
    private void updateAverage(Enrollee enrollee) {
        enrollee.setAverage( (float)enrollee.getEvaluations().stream().mapToDouble(Evaluation::getScore).average().orElse(0.0));
        log.debug("[API] Atualizando média de notas para enrolleeId: {}. Média atual: {}", enrollee.getId(), enrollee.getAverage());
        enrolleeR.save(enrollee);
    }

}