package com.kacademico.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kacademico.app.dto.student.StudentDetailsDTO;
import com.kacademico.app.dto.student.StudentRequestDTO;
import com.kacademico.app.dto.student.StudentResponseDTO;
import com.kacademico.app.dto.student.StudentUpdateDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.mapper.RequestMapper;
import com.kacademico.app.mapper.ResponseMapper;
import com.kacademico.domain.models.Student;
import com.kacademico.domain.repositories.IStudentRepository;
import com.kacademico.domain.repositories.IUserRepository;
import com.kacademico.shared.utils.EnsureUniqueUtil;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StudentService {
    
    private final BCryptPasswordEncoder passwordEncoder;
    private final IStudentRepository studentR;
    private final IUserRepository userR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Transactional
    @Async
    public CompletableFuture<String> createAsync(StudentRequestDTO data) {
        log.info("[API] Iniciando criação de estudante com email: {}", data.user().email());
        EnsureUniqueUtil.ensureUnique(() -> userR.findByEmail(data.user().email()), () -> "An user with email " + data.user().email() + " already exists");

        Student student = requestMapper.toStudent(data);
        student.setPassword(encodePassword(student.getPassword()));
        Student saved = studentR.save(student);

        log.info("[API] Estudante criado com sucesso. ID: {}", saved.getId());
        return CompletableFuture.completedFuture("Student successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<StudentResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(studentR.findAll(), responseMapper::toStudentResponseDTO));
    }

    @Async
    @Transactional
    public CompletableFuture<StudentDetailsDTO> readByIdAsync(UUID id) {
        log.debug("[API] Buscando estudante com ID: {}", id);
        Student student = finder.findByIdOrThrow(studentR.findById(id), "Student not Found");
        
        log.debug("[API] Estudante encontrado: {}", student.getId());
        return CompletableFuture.completedFuture(new StudentDetailsDTO(
            responseMapper.toStudentResponseDTO(student),
            responseMapper.toResponseDTOList(student.getEnrollees(), responseMapper::toEnrolleeResponseDTO)
        ));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, StudentUpdateDTO data) {
        log.info("[API] Atualizando estudante com ID: {}", id);
        Student student = finder.findByIdOrThrow(studentR.findById(id), "Student not Found");
        
        log.info("[API] Estudante atualizado com sucesso. ID: {}", id);
        studentR.save(student);
        return CompletableFuture.completedFuture("Updated Student");
    }

    @Transactional
    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        log.warn("[API] Solicitada exclusão do estudante com ID: {}", id);
        finder.findByIdOrThrow(studentR.findById(id), "Student not Found"); 
        
        log.info("[API] Estudante deletado com sucesso. ID: {}", id);
        studentR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Student");
    }

    private String encodePassword(String password) {
        log.info("[API] Senha será encriptografada");

        if (password == null) return null;
        if (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) return password;  
 
        log.warn("[API] Encriptografando senha");
        return (passwordEncoder.encode(password));
    }

}