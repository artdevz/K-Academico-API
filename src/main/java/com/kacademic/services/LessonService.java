package com.kacademic.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.lesson.LessonRequestDTO;
import com.kacademic.dto.lesson.LessonResponseDTO;
import com.kacademic.dto.lesson.LessonUpdateDTO;
import com.kacademic.models.Lesson;
import com.kacademic.repositories.GradeRepository;
import com.kacademic.repositories.LessonRepository;

@Service
public class LessonService {
    
    private final LessonRepository lessonR;
    private final GradeRepository gradeR;

    private final String entity = "Lesson";

    public LessonService(LessonRepository lessonR, GradeRepository gradeR) {
        this.lessonR = lessonR;
        this.gradeR = gradeR;
    }

    public String create(LessonRequestDTO data) {

        Lesson lesson = new Lesson(
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade")),
            data.topic(),
            data.date()
        );

        lessonR.save(lesson);
        return "Created" + entity;
        
    }

    public List<LessonResponseDTO> readAll() {

        return lessonR.findAll().stream()
            .map(lesson -> new LessonResponseDTO(
                lesson.getId(),
                lesson.getGrade().getId(),
                lesson.getTopic(),                
                lesson.getDate(),
                lesson.getStatus()
            ))
            .collect(Collectors.toList());
    }

    public LessonResponseDTO readById(UUID id) {

        Lesson lesson = lessonR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found."));
        
        return new LessonResponseDTO(
            lesson.getId(),
            lesson.getGrade().getId(),
            lesson.getTopic(),                
            lesson.getDate(),
            lesson.getStatus()
        );
    }

    public String update(UUID id, LessonUpdateDTO data) {

        Lesson lesson = lessonR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, entity + " not Found."));

        lessonR.save(lesson);
        return "Updated" + entity;
        
    }

    public String delete(UUID id) {

        if (!lessonR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found.");
        
        lessonR.deleteById(id);
        return "Deleted" + entity;

    }

}
