package com.kacademic.app.services;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.attendance.AttendanceRequestDTO;
import com.kacademic.app.dto.attendance.AttendanceResponseDTO;
import com.kacademic.app.dto.attendance.AttendanceUpdateDTO;
import com.kacademic.domain.models.Attendance;
import com.kacademic.domain.models.Enrollee;
import com.kacademic.domain.models.Lesson;
import com.kacademic.domain.repositories.AttendanceRepository;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.LessonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AttendanceService {
    
    private final AttendanceRepository attendanceR;
    private final EnrolleeRepository enrolleeR;
    private final LessonRepository lessonR;
    
    private final AsyncTaskExecutor taskExecutor;
    
    @Async
    public CompletableFuture<String> createAsync(AttendanceRequestDTO data) {
        Enrollee enrollee = findEnrolleeDetails(data.enrollee());
        Lesson lesson = findLesson(data.lesson());

        ensureAttendanceNotExists(enrollee, lesson);
        ensureSameGrade(enrollee, lesson);

        Attendance attendance = new Attendance(
            data.isAbsent(),
            enrollee,
            lesson
        );
        
        attendanceR.save(attendance);
        updateAbsences(attendance.getEnrollee());
        return CompletableFuture.completedFuture("Attendance successfully Created: " + attendance.getId());
    }

    @Async
    public CompletableFuture<List<AttendanceResponseDTO>> readAllAsync() {
        return CompletableFuture.completedFuture(
            attendanceR.findAll().stream()
            .map(attendance -> new AttendanceResponseDTO(
                attendance.getId(),
                attendance.getEnrollee().getId(),
                attendance.getLesson().getId(),
                attendance.isAbsent()
            ))
            .collect(Collectors.toList())
        );
    }

    @Async
    public CompletableFuture<AttendanceResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Attendance attendance = findAttendance(id);
            
            return(
                new AttendanceResponseDTO(
                    attendance.getId(),
                    attendance.getEnrollee().getId(),
                    attendance.getLesson().getId(),
                    attendance.isAbsent()
                )
            );
        }, taskExecutor);
    }

    @Async
    public CompletableFuture<String> updateAsync(UUID id, AttendanceUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Attendance attendance = findAttendance(id);
            
            data.isAbsent().ifPresent(attendance::setAbsent);
            if (data.isAbsent().isPresent()) updateAbsences(attendance.getEnrollee());

            attendanceR.save(attendance);
            return "Updated Attendance";
        }, taskExecutor);
    }

    @Async
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Attendance attendance = findAttendance(id);
            
            attendanceR.deleteById(id);
            updateAbsences(attendance.getEnrollee());
            return "Deleted Attendance";
        }, taskExecutor);
    }

    private Attendance findAttendance(UUID id) {
        return attendanceR.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found"));
    }

    private Enrollee findEnrolleeDetails(UUID enrolleeId) {
        return enrolleeR.findByIdWithEvaluationsAndAttendances(enrolleeId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found"));
    }

    private Lesson findLesson(UUID lessonId) {
        return lessonR.findById(lessonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found"));
    }

    private void ensureAttendanceNotExists(Enrollee enrollee, Lesson lesson) {
        if (attendanceR.existsByEnrolleeIdAndLessonId(enrollee.getId(), lesson.getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Attendance already exists for this Enrollee and Exam");
    }

    private void ensureSameGrade(Enrollee enrollee, Lesson lesson) {
        if (!(enrollee.getGrade().getId().equals(lesson.getGrade().getId()))) 
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enrollee and Lesson must belong to the same Grade");
    }

    private void updateAbsences(Enrollee enrollee) {
        enrollee.setAbsences( (int)enrollee.getAttendances().stream().filter(Attendance::isAbsent).count());
        enrolleeR.save(enrollee);
    }

}