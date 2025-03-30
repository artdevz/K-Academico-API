package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.lesson.LessonRequestDTO;
import com.kacademic.app.dto.lesson.LessonResponseDTO;
import com.kacademic.app.dto.lesson.LessonUpdateDTO;
import com.kacademic.domain.models.Lesson;
import com.kacademic.domain.repositories.GradeRepository;
import com.kacademic.domain.repositories.LessonRepository;

@Service
public class LessonService {
    
    private final LessonRepository lessonR;
    private final GradeRepository gradeR;

    public LessonService(LessonRepository lessonR, GradeRepository gradeR) {
        this.lessonR = lessonR;
        this.gradeR = gradeR;
    }

    public String createAsync(LessonRequestDTO data) {
        Lesson lesson = new Lesson(
            gradeR.findById(data.grade()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Grade not Found")),
            data.topic(),
            data.date()
        );
        lessonR.save(lesson);
        return "Created Lesson";
    }

    public List<LessonResponseDTO> readAllAsync() {
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

    public LessonResponseDTO readByIdAsync(UUID id) {
        Lesson lesson = lessonR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found"));
        return new LessonResponseDTO(
            lesson.getId(),
            lesson.getGrade().getId(),
            lesson.getTopic(),
            lesson.getDate(),
            lesson.getStatus()
        );
    }

    public String updateAsync(UUID id, LessonUpdateDTO data) {
        Lesson lesson = lessonR.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found"));
        lessonR.save(lesson);
        return "Updated Lesson";
    }

    public String deleteAsync(UUID id) {
        if (!lessonR.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found");
        }
        lessonR.deleteById(id);
        return "Deleted Lesson";
    }
}
