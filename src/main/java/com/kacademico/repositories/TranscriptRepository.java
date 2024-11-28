package com.kacademico.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kacademico.models.Transcript;

@Repository
public interface TranscriptRepository extends JpaRepository<Transcript, UUID> {
    
}
