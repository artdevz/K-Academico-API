package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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

    public String create(ExamRequestDTO data) {

        Exam exam = new Exam(
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade")),
            data.name(),
            data.maximum(),
            data.date()
        );

        examR.save(exam);
        return "Created " + entity;
        
    }

    public List<ExamResponseDTO> readAll() {

        return examR.findAll().stream()
            .map(exam -> new ExamResponseDTO(
                exam.getId(),
                exam.getGrade().getId(),
                exam.getName(),                
                exam.getMaximum(),
                exam.getDate()
            ))
            .collect(Collectors.toList());
    }

    public ExamResponseDTO readById(UUID id) {

        Exam exam = examR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found."));
        
        return new ExamResponseDTO(
            exam.getId(),
            exam.getGrade().getId(),
            exam.getName(),            
            exam.getMaximum(),
            exam.getDate()
        );
    }

    public String update(UUID id, ExamUpdateDTO data) {

        Exam exam = examR.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
            
        examR.save(exam);
        return "Updated " + entity;
                
    }

    public String delete(UUID id) {

        if (!examR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found.");
        
        examR.deleteById(id);
        return "Deleted " + entity;

    }

}
