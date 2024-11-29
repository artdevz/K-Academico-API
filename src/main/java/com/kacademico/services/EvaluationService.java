package com.kacademico.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.dtos.evaluation.EvaluationRequestDTO;
import com.kacademico.dtos.evaluation.EvaluationResponseDTO;
import com.kacademico.models.Evaluation;
import com.kacademico.repositories.EvaluationRepository;

@Service
public class EvaluationService {
    
    private final EvaluationRepository evaluationR;
    
    private final MappingService mapS;

    public EvaluationService(EvaluationRepository evaluationR, MappingService mapS) {
        this.evaluationR = evaluationR;
        this.mapS = mapS;
    }
    
    public void create(EvaluationRequestDTO data) {

        Evaluation evaluation = new Evaluation(
            mapS.findEnrolleeById(data.enrollee()),
            mapS.findExamById(data.exam()),
            data.score()
        );
        
        addEvaluation(evaluation);
        evaluationR.save(evaluation);

    }

    public List<EvaluationResponseDTO> readAll() {

        return evaluationR.findAll().stream()
            .map(evaluation -> new EvaluationResponseDTO(
                evaluation.getId(),
                evaluation.getEnrollee().getStudent().getUser().getName(),
                evaluation.getExam().getGrade().getId(),
                evaluation.getExam().getName(),
                evaluation.getScore()
            ))
            .collect(Collectors.toList());

    }

    public EvaluationResponseDTO readById(UUID id) {

        Evaluation evaluation = evaluationR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resultado não encontrado."));
        
        return new EvaluationResponseDTO(
            evaluation.getId(),
            evaluation.getEnrollee().getStudent().getUser().getName(),
            evaluation.getExam().getGrade().getId(),
            evaluation.getExam().getName(),
            evaluation.getScore()
        );

    }

    public Evaluation update(UUID id, Map<String, Object> fields) {

        Optional<Evaluation> existingEvalution = evaluationR.findById(id);
    
        if (existingEvalution.isPresent()) {
            Evaluation evalution = existingEvalution.get();            
    
            fields.forEach((key, value) -> {
                switch (key) {
                    
                    case "score":
                        float score = (Float) value;
                        evalution.setScore(score);
                        editEvaluation(evalution); // Atualizar Média
                        break;

                    default:
                        Field field = ReflectionUtils.findField(Evaluation.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, evalution, value);
                        }
                        break;
                }
            });
            
            return evaluationR.save(evalution);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resultado não encontrado.");
        
    }

    public void delete(UUID id) {

        if (!evaluationR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resultado não encontrado.");
        removeEvaluation(evaluationR.findById(id).get());
        evaluationR.deleteById(id);

    }

    private void addEvaluation(Evaluation evaluation) {

        evaluation.getEnrollee().getEvaluations().add(evaluation);
        
        evaluation.getEnrollee().setAvarage( updateAvarage(evaluation.getEnrollee().getEvaluations()) );
    }

    private void editEvaluation(Evaluation evaluation) {

        evaluation.getEnrollee().setAvarage( updateAvarage(evaluation.getEnrollee().getEvaluations()) );

    }    

    private void removeEvaluation(Evaluation evaluation) {

        for (Evaluation currentEvaluation : evaluation.getEnrollee().getEvaluations()) {
            if (currentEvaluation.equals(evaluation)) evaluation.getEnrollee().getEvaluations().remove(evaluation);
        }

        evaluation.getEnrollee().setAvarage( updateAvarage(evaluation.getEnrollee().getEvaluations()) );

    }

    private float updateAvarage(Set<Evaluation> evaluations) {

        float sum = 0;
        for (Evaluation evaluation : evaluations) sum += evaluation.getScore();           
        
        return evaluations.isEmpty() ? 0 : sum / evaluations.size();
        
    }

}
