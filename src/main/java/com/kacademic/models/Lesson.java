package com.kacademic.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kacademic.enums.ELesson;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "lessons")
@Entity
public class Lesson implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    // Simple Attributes
    private String name;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private ELesson status;

    // Constructor
    public Lesson(String name, String description, LocalDate date) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.status = ELesson.UPCOMING; // Default Status
    }

}
