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
import com.kacademic.repositories.LessonRepository;

@Service
public class LessonService {
    
    private final LessonRepository lessonR;

    private final MappingService mapS;

    private final String entity = "Lesson";

    public LessonService(LessonRepository lessonR, MappingService mapS) {
        this.lessonR = lessonR;
        this.mapS = mapS;
    }

    public String create(LessonRequestDTO data) {

        Lesson lesson = new Lesson(
            mapS.findGradeById(data.grade()),
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
