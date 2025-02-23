package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.exam.ExamRequestDTO;
import com.kacademic.dto.exam.ExamResponseDTO;
import com.kacademic.models.Exam;
import com.kacademic.repositories.ExamRepository;

@Service
public class ExamService {
    
    private final ExamRepository examR;

    private final MappingService mapS;

    public ExamService(ExamRepository examR, MappingService mapS) {
        this.examR = examR;
        this.mapS = mapS;
    }

    public void create(ExamRequestDTO data) {

        Exam exam = new Exam(
            mapS.findGradeById(data.grade()),
            data.name(),
            data.maximum(),
            data.date()
        );

        examR.save(exam);
        
    }

    public List<ExamResponseDTO> readAll() {

        return examR.findAll().stream()
            .map(exam -> new ExamResponseDTO(
                exam.getId(),
                exam.getGrade().getSubject().getName(),
                exam.getName(),                
                exam.getMaximum(),
                exam.getDate()
            ))
            .collect(Collectors.toList());
    }

    public ExamResponseDTO readById(UUID id) {

        Exam exam = examR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada."));
        
        return new ExamResponseDTO(
            exam.getId(),
            exam.getGrade().getSubject().getName(),
            exam.getName(),            
            exam.getMaximum(),
            exam.getDate()
        );
    }

    public Exam update(UUID id, Map<String, Object> fields) {

        Optional<Exam> existingExam = examR.findById(id);
    
        if (existingExam.isPresent()) {
            Exam exam = existingExam.get();
    
            fields.forEach((key, value) -> {
                switch (key) {                

                    default:
                        Field field = ReflectionUtils.findField(Exam.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, exam, value);
                        }
                        break;
                }
            });
            
            return examR.save(exam);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada.");
        
    }

    public void delete(UUID id) {

        if (!examR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avaliação não encontrada.");
        examR.deleteById(id);

    }

}
