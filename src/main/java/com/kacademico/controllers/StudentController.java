/* package com.kacademico.controllers;

import java.util.List;
import java.util.Map;
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

import com.kacademico.dtos.user.StudentRequestDTO;
import com.kacademico.dtos.user.StudentResponseDTO;
import com.kacademico.services.StudentService;

import jakarta.validation.Valid;

@RequestMapping("/student")
@RestController
public class StudentController {
    
    private final StudentService studentS;

    public StudentController(StudentService studentS) {
        this.studentS = studentS;
    }
    
    @PostMapping
    public ResponseEntity<String> createStudent(@RequestBody @Valid StudentRequestDTO data) {

        studentS.create(data);

        return new ResponseEntity<>("Created Student.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<StudentResponseDTO>> readAllStudent() {

        return new ResponseEntity<>(studentS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> findStudentById(@PathVariable UUID id) {

        StudentResponseDTO student = studentS.readById(id);

        return new ResponseEntity<>(student, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> updateStudent(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        studentS.update(id, fields);
        return new ResponseEntity<>("Updated Student", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable UUID id) {
        
        studentS.delete(id);
        return new ResponseEntity<>("Deleted Student", HttpStatus.OK);
           
    }
    
}*/
