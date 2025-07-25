package com.kacademico.infra.repositories.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademico.infra.entities.AttendanceEntity;

public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, UUID> {

    boolean existsByEnrolleeIdAndLessonId(UUID enrolleeId, UUID lessonId);
    
}
