package com.kacademico.app.services;

import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.domain.models.values.Enrollment;
import com.kacademico.domain.repositories.IStudentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollmentGeneratorService {
    
    private final IStudentRepository studentR;

    public Enrollment generate(String courseCode) {
        int year = Year.now().getValue() % 100; // 2025 -> 25
        int semester = (LocalDate.now().getMonthValue() <= 6) ? 1 : 2;

        log.debug("[API] Gerando matrícula para curso: {}, ano: {}, semestre: {}", courseCode, year, semester);

        List<String> allPossible = IntStream.rangeClosed(100, 999)
            .mapToObj(i -> String.format("%02d%d.%s.%03d", year, semester, courseCode, i))
            .collect(Collectors.toList());

        Set<String> usedEnrollments = studentR.findAllEnrollmentsByPrefix(
            String.format("%02d%d.%s.", year, semester, courseCode));

        log.info("[API] Matrículas já usadas para prefixo {}: {}", 
            String.format("%02d%d.%s.", year, semester, courseCode), usedEnrollments.size());

        List<String> available = allPossible.stream()
            .filter(e -> !usedEnrollments.contains(e))
            .collect(Collectors.toList());

        if (available.isEmpty()) {
            log.error("[API] Não há vagas disponíveis para matrícula no curso {} no semestre {}/{}", courseCode, year, semester);
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No enrollment slots are available for this course and semester");
        }

        Collections.shuffle(available);        
        log.info("[API] Matrícula gerada com sucesso: {}", available.get(0));
        return new Enrollment(available.get(0));
    }

}
