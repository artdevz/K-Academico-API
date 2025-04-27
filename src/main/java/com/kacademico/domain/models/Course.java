package com.kacademico.domain.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="courses")
@Entity
public class Course implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    @Size(min=4, max=160, message="Course name must be between 4 and 160 characters")
    private String name;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^\\d+$", message = "Course code must contain only numbers")
    @Size(min=3, max=3, message="Course code must contain exactly 3 characters")
    private String code;
    
    private int duration; // In Hours.

    @Size(max=256)
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE, orphanRemoval = false)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE, orphanRemoval = false)
    private List<Subject> subjects = new ArrayList<>();

    public Course(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.duration = 0;
        this.description = description;
    }

}
