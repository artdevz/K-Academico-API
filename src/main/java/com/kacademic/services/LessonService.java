package com.kacademic.services;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.dto.lesson.LessonRequestDTO;
import com.kacademic.dto.lesson.LessonResponseDTO;
import com.kacademic.models.Lesson;
import com.kacademic.repositories.LessonRepository;

@Service
public class LessonService {
    
    private final LessonRepository lessonR;

    private final MappingService mapS;

    public LessonService(LessonRepository lessonR, MappingService mapS) {
        this.lessonR = lessonR;
        this.mapS = mapS;
    }

    public void create(LessonRequestDTO data) {

        Lesson lesson = new Lesson(
            mapS.findGradeById(data.grade()),
            data.topic(),
            data.date()
        );

        lessonR.save(lesson);
        
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

    public Lesson update(UUID id, Map<String, Object> fields) {

        Optional<Lesson> existingLesson = lessonR.findById(id);
    
        if (existingLesson.isPresent()) {
            Lesson lesson = existingLesson.get();
    
            fields.forEach((key, value) -> {
                switch (key) {                

                    default:
                        Field field = ReflectionUtils.findField(Lesson.class, key);
                        if (field != null) {
                            field.setAccessible(true);
                            ReflectionUtils.setField(field, lesson, value);
                        }
                        break;
                }
            });
            
            return lessonR.save(lesson);
        } 
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found.");
        
    }

    public void delete(UUID id) {

        if (!lessonR.findById(id).isPresent()) 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found.");
        lessonR.deleteById(id);

    }

}
