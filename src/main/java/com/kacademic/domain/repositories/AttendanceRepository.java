package com.kacademic.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademic.domain.models.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    boolean existsByEnrolleeIdAndLessonId(UUID enrolleeId, UUID lessonId);

}
