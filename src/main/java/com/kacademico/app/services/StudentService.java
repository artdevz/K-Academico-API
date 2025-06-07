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

@RequiredArgsConstructor
@Service
public class StudentService {
    
    private final BCryptPasswordEncoder passwordEncoder;
    private final IStudentRepository studentR;
    private final IUserRepository userR;
    private final RequestMapper requestMapper;
    private final ResponseMapper responseMapper;
    private final EntityFinder finder;

    @Async
    public CompletableFuture<String> createAsync(StudentRequestDTO data) {
        EnsureUniqueUtil.ensureUnique(() -> userR.findByEmail(data.user().email()), () -> "An user with email " + data.user().email() + " already exists");

        Student student = requestMapper.toStudent(data);
        student.setPassword(encodePassword(student.getPassword()));
        Student saved = studentR.save(student);

        return CompletableFuture.completedFuture("Student successfully Created: " + saved.getId());
    }

    @Async
    public CompletableFuture<List<StudentResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(responseMapper.toResponseDTOList(studentR.findAll(), responseMapper::toStudentResponseDTO));
    }

    @Async
    @Transactional
    public CompletableFuture<StudentDetailsDTO> readByIdAsync(UUID id) {
        Student student = finder.findByIdOrThrow(studentR.findById(id), "Student not Found");
        
        return CompletableFuture.completedFuture(new StudentDetailsDTO(
            responseMapper.toStudentResponseDTO(student),
            responseMapper.toResponseDTOList(student.getEnrollees(), responseMapper::toEnrolleeResponseDTO)
        ));
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, StudentUpdateDTO data) {
        Student student = finder.findByIdOrThrow(studentR.findById(id), "Student not Found");
            
        studentR.save(student);
        return CompletableFuture.completedFuture("Updated Student");
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        finder.findByIdOrThrow(studentR.findById(id), "Student not Found"); 
        
        studentR.deleteById(id);
        return CompletableFuture.completedFuture("Deleted Student");
    }

    private String encodePassword(String password) {
        if (password == null) return null;
        if (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$")) return password;  
        return (passwordEncoder.encode(password));
    }

}