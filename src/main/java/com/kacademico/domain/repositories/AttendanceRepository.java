package com.kacademico.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.domain.models.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    boolean existsByEnrolleeIdAndLessonId(UUID enrolleeId, UUID lessonId);

}
