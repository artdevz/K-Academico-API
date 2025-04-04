package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.professor.ProfessorRequestDTO;
import com.kacademic.app.dto.professor.ProfessorResponseDTO;
import com.kacademic.app.dto.professor.ProfessorUpdateDTO;
import com.kacademic.domain.models.Professor;
import com.kacademic.domain.repositories.ProfessorRepository;
import com.kacademic.domain.repositories.RoleRepository;

@Service
public class ProfessorService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private final ProfessorRepository professorR;
    private final RoleRepository roleR;

    public ProfessorService(ProfessorRepository professorR, RoleRepository roleR) {
        this.professorR = professorR;
        this.roleR = roleR;
    }

    public String createAsync(ProfessorRequestDTO data) {
        Professor professor = new Professor(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),
            data.user().roles().stream()
                .map(roleId -> roleR.findById(roleId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
                .collect(Collectors.toSet()),
            data.wage()
        );

        professorR.save(professor);
        return "Created Professor";
    }

    public List<ProfessorResponseDTO> readAllAsync() {
        return professorR.findAll().stream()
            .map(professor -> new ProfessorResponseDTO(
                professor.getId(),
                professor.getName(),
                professor.getEmail(),
                professor.getWage()
            ))
            .collect(Collectors.toList()
        );
    }

    public ProfessorResponseDTO readByIdAsync(UUID id) {
        Professor professor = professorR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not Found"));

        return new ProfessorResponseDTO(
            professor.getId(),
            professor.getName(),
            professor.getEmail(),
            professor.getWage()
        );
    }

    public String updateAsync(UUID id, ProfessorUpdateDTO data) {
        Professor professor = professorR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not Found"));

        data.wage().ifPresent(professor::setWage);
        
        professorR.save(professor);
        return "Updated Professor";
    }

    public String deleteAsync(UUID id) {
        if (!professorR.findById(id).isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not Found");
        
        professorR.deleteById(id);
        return "Deleted Professor";
    }
}