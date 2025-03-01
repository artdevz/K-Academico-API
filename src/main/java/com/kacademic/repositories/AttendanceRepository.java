package com.kacademic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kacademic.models.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {}
