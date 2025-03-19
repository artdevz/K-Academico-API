package com.kacademic.services;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.evaluation.EvaluationRequestDTO;
import com.kacademic.dto.evaluation.EvaluationResponseDTO;
import com.kacademic.dto.evaluation.EvaluationUpdateDTO;
import com.kacademic.models.Evaluation;
import com.kacademic.repositories.EvaluationRepository;

@Service
public class EvaluationService {
    
    private final EvaluationRepository evaluationR;
    
    private final MappingService mapS;

    private final String entity = "Evaluation";

    public EvaluationService(EvaluationRepository evaluationR, MappingService mapS) {
        this.evaluationR = evaluationR;
        this.mapS = mapS;
    }
    
    public String create(EvaluationRequestDTO data) {

        Evaluation evaluation = new Evaluation(
            mapS.findEnrolleeById(data.enrollee()),
            mapS.findExamById(data.exam()),
            data.score()
        );
        
        addEvaluation(evaluation);
        evaluationR.save(evaluation);
        return "Created" + entity;

    }

    public List<EvaluationResponseDTO> readAll() {

        return evaluationR.findAll().stream()
            .map(evaluation -> new EvaluationResponseDTO(
                evaluation.getId(),
                evaluation.getEnrollee().getId(),
                evaluation.getExam().getGrade().getId(),
                evaluation.getExam().getId(),
                evaluation.getScore()
            ))
            .collect(Collectors.toList());

    }

    public EvaluationResponseDTO readById(UUID id) {

        Evaluation evaluation = evaluationR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation not Found."));
        
        return new EvaluationResponseDTO(
            evaluation.getId(),
            evaluation.getEnrollee().getId(),
            evaluation.getExam().getGrade().getId(),
            evaluation.getExam().getId(),
            evaluation.getScore()
        );

    }

    public String update(UUID id, EvaluationUpdateDTO data) {

        Evaluation evaluation = evaluationR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
        
        data.score().ifPresent(evaluation::setScore);
        if (data.score().isPresent()) editEvaluation(evaluation);
            
        evaluationR.save(evaluation);
        return "Updated" + entity;
                 
    }

    public String delete(UUID id) {

        if (!evaluationR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Evaluation not Found.");
        
        removeEvaluation(evaluationR.findById(id).get());
        evaluationR.deleteById(id);
        return "Deleted" + entity;

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
