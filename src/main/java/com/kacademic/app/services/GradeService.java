package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kacademic.app.dto.grade.GradeRequestDTO;
import com.kacademic.app.dto.grade.GradeResponseDTO;
import com.kacademic.app.dto.grade.GradeUpdateDTO;
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.app.mapper.RequestMapper;
import com.kacademic.app.mapper.ResponseMapper;
import com.kacademic.domain.enums.EGrade;
import com.kacademic.domain.models.Grade;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.GradeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GradeService {
    
    private final GradeRepository gradeR;
    private final EnrolleeRepository enrolleeR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;  
    
    private final SemesterService semesterS;
    
    @Async
    public CompletableFuture<String> createAsync(GradeRequestDTO data) {
        Grade grade = requestMapper.toGrade(data);

        gradeR.save(grade);
        return CompletableFuture.completedFuture("Grade successfully Created: " + grade.getId());
    }

    @Async
    public CompletableFuture<List<GradeResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(gradeR.findAll(), responseMapper::toGradeResponseDTO));
    }

    @Async
    public CompletableFuture<GradeResponseDTO> readByIdAsync(UUID id) {        
        return CompletableFuture.completedFuture(responseMapper.toGradeResponseDTO(finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found")));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, GradeUpdateDTO data) {
        Grade grade = finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found");
    
        data.status().ifPresent(grade::setStatus);
        if (data.status().isPresent() && data.status().get().equals(EGrade.FINAL)) semesterS.processPartialResults(id);

        grade.setCurrentStudents(grade.getEnrollees().size()); // Atualiza o Número de Estudantes.
        
        gradeR.save(grade);
        return CompletableFuture.completedFuture("Updated Grade");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(gradeR.findById(id), "Grade not Found");
        
        enrolleeR.removeGradeFromEnrollees(id);
        gradeR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Grade");
    }

}