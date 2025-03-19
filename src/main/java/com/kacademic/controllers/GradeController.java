package com.kacademic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.dto.grade.GradeRequestDTO;
import com.kacademic.dto.grade.GradeResponseDTO;
import com.kacademic.dto.grade.GradeUpdateDTO;
import com.kacademic.services.GradeService;
import com.kacademic.services.SemesterService;

import jakarta.validation.Valid;

@RequestMapping("/grade")
@RestController
public class GradeController {
    
    private final GradeService gradeS;
    private final SemesterService semesterS;

    public GradeController(GradeService gradeS, SemesterService semesterS) {
        this.gradeS = gradeS;
        this.semesterS = semesterS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid GradeRequestDTO request) {
        return new ResponseEntity<>(gradeS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<GradeResponseDTO>> readAll() {
        return new ResponseEntity<>(gradeS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponseDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(gradeS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid GradeUpdateDTO data) {
        return new ResponseEntity<>(gradeS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(gradeS.delete(id), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> partialSubmit(@PathVariable UUID id) {
        semesterS.partialSubmit(id);
        return new ResponseEntity<>("Finalizado parcialmente as Atividades da Turma.", HttpStatus.OK);
    }

}
