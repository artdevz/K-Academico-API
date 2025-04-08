package com.kacademic.app.services;

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

import com.kacademic.domain.models.values.Enrollment;
import com.kacademic.domain.repositories.StudentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EnrollmentGeneratorService {
    
    private final StudentRepository studentR;

    public Enrollment generate(String courseCode) {
        int year = Year.now().getValue() % 100; // 2025 -> 25
        int semester = (LocalDate.now().getMonthValue() <= 6)? 1 : 2;

        List<String> allPossible = IntStream.rangeClosed(100, 999)
            .mapToObj(i -> String.format("%02d%d.%s.%03d", year, semester, courseCode, i))
            .collect(Collectors.toList());

        Set<String> usedEnrollments = studentR.findAllEnrollmentsByPrefix(String.format("%02d%d.%s.", year, semester, courseCode));

        List<String> available = allPossible.stream().filter(e -> !usedEnrollments.contains(e)).collect(Collectors.toList());

        if (available.isEmpty()) throw new ResponseStatusException(HttpStatus.CONFLICT, "No enrollment slots are available for this course and semester");
        System.out.println("Matriculados: " + usedEnrollments.size());
        Collections.shuffle(available);
        return new Enrollment(available.get(0));
    }

}
