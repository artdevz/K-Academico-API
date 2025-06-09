package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.app.dto.enrollee.EnrolleeDetailsDTO;
import com.kacademico.app.dto.enrollee.EnrolleeRequestDTO;
import com.kacademico.app.dto.enrollee.EnrolleeResponseDTO;
import com.kacademico.app.dto.enrollee.EnrolleeUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.models.Grade;
import com.kacademico.domain.models.Student;
import com.kacademico.domain.repositories.IEnrolleeRepository;
import com.kacademico.domain.repositories.IGradeRepository;
import com.kacademico.domain.repositories.IStudentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrolleeService {
    
    private final IEnrolleeRepository enrolleeR;
    private final IStudentRepository studentR;
    private final IGradeRepository gradeR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(EnrolleeRequestDTO data) {
        log.info("[API] Criando matrícula para o estudante ID: {}", data.student());
        Enrollee enrollee = requestMapper.toEnrollee(data);

        ensureStudentIsUnique(enrollee.getStudent());
        
        Enrollee saved = enrolleeR.save(enrollee);
        updateGrade(saved.getGrade());
        log.info("[API] Matrícula criada com sucesso: {}", saved.getId());
        return CompletableFuture.completedFuture("Enrollee successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<EnrolleeResponseDTO>> readAllAsync() {
        log.debug("[API] Buscando todas as matrículas");

        List<EnrolleeResponseDTO> response = responseMapper.toResponseDTOList(enrolleeR.findAll(), responseMapper::toEnrolleeResponseDTO);
        
        log.debug("[API] Encontradas {} matrículas", response.size());
        return CompletableFuture.completedFuture(response);
    }

    @Transactional
    @Async
    public CompletableFuture<EnrolleeDetailsDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscando matrícula com ID: {}", id);
        Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findById(id), "Enrollee not Found");
        
        log.debug("[API] Matrícula encontrada: {}", enrollee.getId());
        return CompletableFuture.completedFuture(
            new EnrolleeDetailsDTO(
                responseMapper.toEnrolleeResponseDTO(enrollee),
                responseMapper.toResponseDTOList(enrollee.getEvaluations().stream().toList(), responseMapper::toEvaluationResponseDTO),
                responseMapper.toResponseDTOList(enrollee.getAttendances().stream().toList(), responseMapper::toAttendanceResponseDTO)
            )
        );
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, EnrolleeUpdateDTO data) {
        log.info("[API] Atualizando matrícula com ID: {}", id);
        Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findById(id), "Enrollee not Found");
        
        enrolleeR.save(enrollee);
        log.info("[API] Matrícula atualizada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Updated Enrollee");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.warn("[API] Solicitada exclusão da matrícula com ID: {}", id);
        Enrollee enrollee = finder.findByIdOrThrow(enrolleeR.findWithEvaluationsAndAttendancesById(id), "Enrollee not Found");
        
        enrolleeR.deleteById(id);
        updateGrade(enrollee.getGrade());
        log.info("[API] Matrícula deletada com sucesso. ID: {}", id);
        return CompletableFuture.completedFuture("Deleted Enrollee");
    }

    private void ensureStudentIsUnique(Student student) {
        if (studentR.findById(student.getId()).isPresent()) {
            log.error("[API] Estudante já matriculado para esta Turma. Student ID: {}", student.getId());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already exists for this Grade");
        }
    }

    private void updateGrade(Grade grade) {
        int count = (int) grade.getEnrollees().stream().count();
        grade.setCurrentStudents(count);
        log.info("[API] Atualizando Turma ID {}: Alunos atuais = {}", grade.getId(), count);
        gradeR.save(grade);
    }
}
