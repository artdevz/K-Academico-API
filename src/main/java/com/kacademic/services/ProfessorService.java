package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.professor.ProfessorRequestDTO;
import com.kacademic.dto.professor.ProfessorResponseDTO;
import com.kacademic.models.Professor;
import com.kacademic.repositories.ProfessorRepository;

@Service
public class ProfessorService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final ProfessorRepository professorR;

    public ProfessorService(ProfessorRepository professorR, MappingService mapS) {
        this.professorR = professorR;
    }

    public void create(ProfessorRequestDTO data) {

        Professor professor = new Professor(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),            
            data.wage()            
        );

        professorR.save(professor);
        
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
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado."));
        
        return new ProfessorResponseDTO(
            professor.getId(),                
            professor.getName(),
            professor.getEmail(),
            professor.getWage()
        );
    }

    public Professor update(UUID id, Map<String, Object> fields) {

        Optional<Professor> existingProfessor = professorR.findById(id);
    
        if (existingProfessor.isPresent()) {
            Professor professor = existingProfessor.get();
    
            fields.forEach((key, value) -> {
                switch (key) {

                    // case "name":
                    //     String name = (String) value;
                    //     user.setName(name);
                    //     break;                    

                    default:
                        Field field = ReflectionUtils.findField(Professor.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, professor, value);
                        }
                        break;
                }
            });
            
            return professorR.save(professor);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado.");
        
    }

    public void delete(UUID id) {

        if (!professorR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado.");
            professorR.deleteById(id);

    }

}
