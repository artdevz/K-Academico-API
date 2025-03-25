package com.kacademic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Subject> subjects = new ArrayList<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Student> students = new ArrayList<>();

    @Size(min=4, max=160, message="Course name must be between 4 and 160 characters")
    private String name;

    @Pattern(regexp = "^\\d+$", message = "Course code must contain only numbers")
    @Size(min=2, max=2, message="Course code must contain exactly 2 characters")
    private String code;
    
    @Min(value = 0, message = "Duration must not be less than zero")
    private int duration; // In Hours.

    @Size(max = 256, message = "Description must not exceed 256 characters")
    private String description;

    // Constructor
    public Course(String name, String code, String description) {
        this.name = name;
        this.code = code;
        this.duration = 0; // Starts with 0 Subjects :. 0 Hours
        this.description = description;
    }

}
