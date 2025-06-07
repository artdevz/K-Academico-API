package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kacademico.domain.models.values.Workload;

public class Course {

    private static final int MAX_NAME_LENGHT = 128;
    private static final int MIN_NAME_LENGHT = 8;
    private static final int MAX_DESCRIPTION_LENGHT = 255;

    private UUID id;
    private String name;
    private String code;
    private String description;
    private Workload workload; // Medido em horas

    private List<Student> students = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();

    public Course() {}

    public Course(UUID id, String name, String code, String description, Workload workload) {
        this.id = id;
        setName(name);
        setCode(code);
        setDescription(description);
        setWorkload(workload);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getDescription() { return description; }
    public Workload getWorkload() { return workload; }
    public List<Student> getStudents() { return students; }
    public List<Subject> getSubjects() { return subjects; }

    public void setName(String name) {
        if (name == null || name.length() < MIN_NAME_LENGHT || name.length() > MAX_NAME_LENGHT) {
            throw new IllegalArgumentException("Course name must be between " + MIN_NAME_LENGHT + " and " + MAX_NAME_LENGHT + " characters");
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
        if (description == null || description.length() > MAX_DESCRIPTION_LENGHT) {
            throw new IllegalArgumentException("Description must be at most " + MAX_DESCRIPTION_LENGHT + " characters");
        }
        this.description = description;
    }

    public void setWorkload(Workload workload) {
        if (workload == null) throw new IllegalArgumentException("Workload cannot be null");
        this.workload = workload;
    }

    public void addSubject(Subject subject) {
        if (subject == null) subjects = new ArrayList<>();
        if (!subjects.contains(subject)) {
            subjects.add(subject);
            subject.setCourse(this);
        }
    }

    public void removeSubject(Subject subject) {
        if (subjects != null && subjects.remove(subject)) subject.setCourse(null);
    }

}
