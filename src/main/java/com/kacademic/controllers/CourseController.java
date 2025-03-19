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

import com.kacademic.dto.course.CourseDetailsDTO;
import com.kacademic.dto.course.CourseRequestDTO;
import com.kacademic.dto.course.CourseResponseDTO;
import com.kacademic.dto.course.CourseUpdateDTO;
import com.kacademic.services.CourseService;

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
        return new ResponseEntity<>(courseS.create(request), HttpStatus.CREATED);
    }
    
    @GetMapping    
    public ResponseEntity<List<CourseResponseDTO>> readAll() {
        return new ResponseEntity<>(courseS.readAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDTO> readById(@PathVariable UUID id) {
        return new ResponseEntity<>(courseS.readById(id), HttpStatus.OK);
    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody @Valid CourseUpdateDTO data) {
        return new ResponseEntity<>(courseS.update(id, data), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(courseS.delete(id), HttpStatus.OK);
    }

}
