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
    
    @Async("taskExecutor")
    public CompletableFuture<String> createAsync(AttendanceRequestDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Attendance attendance = new Attendance(
                enrolleeR.findById(data.enrollee()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollee not Found")),
                lessonR.findById(data.lesson()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not Found")),
                data.isAbsent()
            );
            
            attendanceR.save(attendance);
            return "Created Attendace";

        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<AttendanceResponseDTO>> readAllAsync() {
        return CompletableFuture.supplyAsync(() -> {
            return(
                attendanceR.findAll().stream()
                .map(attendance -> new AttendanceResponseDTO(
                    attendance.getId(),
                    attendance.getEnrollee().getId(),
                    attendance.getLesson().getId(),
                    attendance.isAbsent()
                ))
                .collect(Collectors.toList())
            );
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<AttendanceResponseDTO> readByIdAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            Attendance attendance = attendanceR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found"));
            
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

    @Async("taskExecutor")
    public CompletableFuture<String> updateAsync(UUID id, AttendanceUpdateDTO data) {
        return CompletableFuture.supplyAsync(() -> {
            Attendance attendance = attendanceR.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found"));
                
            attendanceR.save(attendance);
            return "Updated Attendance";
        }, taskExecutor);
    }

    @Async("taskExecutor")
    public CompletableFuture<String> deleteAsync(UUID id) {
        return CompletableFuture.supplyAsync(() -> {
            if (!attendanceR.findById(id).isPresent()) 
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attendance not Found");
            
            attendanceR.deleteById(id);
            return "Deleted Attendance";
        }, taskExecutor);
    }

}