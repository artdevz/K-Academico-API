package com.kacademico.controllers;

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

import com.kacademico.dtos.course.CourseRequestDTO;
import com.kacademico.dtos.course.CourseResponseDTO;
import com.kacademico.services.CourseService;

import jakarta.validation.Valid;

@RequestMapping("/course")
@RestController
public class CourseController {
    
    private final CourseService courseS;

    public CourseController(CourseService courseS) {
        this.courseS = courseS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid CourseRequestDTO request) {

        courseS.create(request);

        return new ResponseEntity<>("Created Course.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<CourseResponseDTO>> readAll() {

        return new ResponseEntity<>(courseS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> readById(@PathVariable UUID id) {

        CourseResponseDTO response = courseS.readById(id);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        courseS.update(id, fields);
        return new ResponseEntity<>("Updated Course", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        courseS.delete(id);
        return new ResponseEntity<>("Deleted Course", HttpStatus.OK);
           
    }

}
