package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Course {

    private UUID id;
    private String name;
    private String code;
    private String description;
    private int duration; // Medido em horas

    private List<Student> students = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();

    public Course() {}

    public Course(UUID id, String name, String code, String description, int duration) {
        this.id = id;
        setName(name);
        setCode(code);
        setDescription(description);
        setDuration(duration);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public int getDuration() { return duration; }
    public String getDescription() { return description; }
    public List<Student> getStudents() { return students; }
    public List<Subject> getSubjects() { return subjects; }

    public void setName(String name) {
        if (name == null || name.length() < 8 || name.length() > 128) {
            throw new IllegalArgumentException("Course name must be between 8 and 128 characters");
        }
        this.name = name;
    }

    public void setCode(String code) {
        if (code == null || !code.matches("^\\d{3}$")) {
            throw new IllegalArgumentException("Course code must contain exactly 3 digits");
        }
        this.code = code;
    }

    public void setDescription(String description) {
        if (description == null || description.length() > 255) {
            throw new IllegalArgumentException("Description must be at most 255 characters");
        }
        this.description = description;
    }

    public void setDuration(int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration must be non-negative");
        }
        this.duration = duration;
    }

}
