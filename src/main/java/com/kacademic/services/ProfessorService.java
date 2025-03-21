package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.professor.ProfessorRequestDTO;
import com.kacademic.dto.professor.ProfessorResponseDTO;
import com.kacademic.dto.professor.ProfessorUpdateDTO;
import com.kacademic.models.Professor;
import com.kacademic.repositories.ProfessorRepository;

@Service
public class ProfessorService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ProfessorRepository professorR;

    private final String entity = "Professor";

    public ProfessorService(ProfessorRepository professorR) {
        this.professorR = professorR;
    }

    public String create(ProfessorRequestDTO data) {

        Professor professor = new Professor(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),            
            data.wage()            
        );

        professorR.save(professor);
        return "Created " + entity;
        
    }

    public List<ProfessorResponseDTO> readAll() {

        return professorR.findAll().stream()
            .map(professor -> new ProfessorResponseDTO(
                professor.getId(),                
                professor.getName(),
                professor.getEmail(),
                professor.getWage()
            ))
            .collect(Collectors.toList());
    }

    public ProfessorResponseDTO readById(UUID id) {

        Professor professor = professorR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
        
        return new ProfessorResponseDTO(
            professor.getId(),                
            professor.getName(),
            professor.getEmail(),
            professor.getWage()
        );
    }

    public String update(UUID id, ProfessorUpdateDTO data) {

        Professor professor = professorR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));
        
        data.wage().ifPresent(professor::setWage);

        professorR.save(professor);
        return "Updated " + entity;
                
    }

    public String delete(UUID id) {

        if (!professorR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found.");
        
        professorR.deleteById(id);
        return "Deleted " + entity;

    }

}
