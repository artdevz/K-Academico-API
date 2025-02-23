package com.kacademic.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kacademic.services.SemesterService;
import com.kacademic.utils.Semester;

@RequestMapping("/semester")
@RestController
public class SemesterController {
    
    private final SemesterService semesterS;

    public SemesterController(SemesterService semesterS) {
        this.semesterS = semesterS;
    }

    @PostMapping("/{semester}")
    public ResponseEntity<String> finalSubmit(@PathVariable @Semester String semester) {

        semesterS.finalSubmit(semester);
        return new ResponseEntity<>("Finalizado todas as Turmas do Semestre " + semester, HttpStatus.OK);

    }

}
