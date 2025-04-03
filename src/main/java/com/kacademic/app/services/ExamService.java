package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.exam.ExamRequestDTO;
import com.kacademic.app.dto.exam.ExamResponseDTO;
import com.kacademic.app.dto.exam.ExamUpdateDTO;
import com.kacademic.domain.models.Exam;
import com.kacademic.domain.repositories.ExamRepository;
import com.kacademic.domain.repositories.GradeRepository;

@Service
public class ExamService {
    
    private final ExamRepository examR;
    private final GradeRepository gradeR;

    public ExamService(ExamRepository examR, GradeRepository gradeR) {
        this.examR = examR;
        this.gradeR = gradeR;
    }

    public String createAsync(ExamRequestDTO data) {
        Exam exam = new Exam(
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found")),
            data.name(),
            data.maximum(),
            data.date()
        );

        examR.save(exam);
        return "Created Exam";
    }

    public List<ExamResponseDTO> readAllAsync() {
        return (
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

    public ExamResponseDTO readByIdAsync(UUID id) {
        Exam exam = examR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found"));
        
        return (
            new ExamResponseDTO(
                exam.getId(),
                exam.getGrade().getId(),
                exam.getName(),            
                exam.getMaximum(),
                exam.getDate()
        ));
    }

    public String updateAsync(UUID id, ExamUpdateDTO data) {
        Exam exam = examR.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found"));
            
        examR.save(exam);
        return "Updated Exam";
    }

    public String deleteAsync(UUID id) {
        if (!examR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not Found");
        
        examR.deleteById(id);
        return "Deleted Exam";
    }
}