package com.kacademic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademic.models.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, UUID> {}
