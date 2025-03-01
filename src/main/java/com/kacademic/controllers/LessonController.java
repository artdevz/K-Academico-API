package com.kacademic.controllers;

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

import com.kacademic.dto.lesson.LessonRequestDTO;
import com.kacademic.dto.lesson.LessonResponseDTO;
import com.kacademic.services.LessonService;

import jakarta.validation.Valid;

@RequestMapping("/lesson")
@RestController
public class LessonController {
    
    private final LessonService lessonS;

    public LessonController(LessonService lessonS) {
        this.lessonS = lessonS;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid LessonRequestDTO request) {

        lessonS.create(request);

        return new ResponseEntity<>("Created Lesson.", HttpStatus.CREATED);

    }
    
    @GetMapping    
    public ResponseEntity<List<LessonResponseDTO>> readAll() {

        return new ResponseEntity<>(lessonS.readAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonResponseDTO> readById(@PathVariable UUID id) {

        return new ResponseEntity<>(lessonS.readById(id), HttpStatus.OK);

    }    
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@RequestBody Map<String, Object> fields, @PathVariable UUID id) {
        
        lessonS.update(id, fields);
        
        return new ResponseEntity<>("Updated Lesson", HttpStatus.OK);
        
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        
        lessonS.delete(id);

        return new ResponseEntity<>("Deleted Lesson", HttpStatus.OK);
           
    }

}
