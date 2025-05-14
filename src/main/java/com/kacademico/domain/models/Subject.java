package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Subject {

    private UUID id;    
    private String name;
    private String description;
    private int duration;
    private int semester;
    private boolean isRequired;
    
    private Course course;
    
    private List<Grade> grades = new ArrayList<>();    
    private List<Equivalence> prerequisites = new ArrayList<>();

    public Subject() {}

    public Subject(UUID id, String name, String description, int duration, int semester, boolean isRequired, Course course) {
        this.id = id;
        setName(name);
        setDescription(description);
        setDuration(duration);
        setSemester(semester);
        setRequired(isRequired);
        this.course = course;
    }
    
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public int getSemester() { return semester; }
    public boolean isRequired() { return isRequired; }
    public Course getCourse() { return course; }
    public List<Grade> getGrades() { return grades; }
    public List<Equivalence> getPrerequisites() { return prerequisites; }

    public void setName(String name) {
        if (name == null || name.length() > 128) throw new IllegalArgumentException("Name must be at most 256 characters"); 
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null || description.length() > 256) throw new IllegalArgumentException("Description must be at most 256 characters"); 
        this.description = description;
    }

    private void setDuration(int duration) {
        if (duration < 40 || duration > 80) throw new IllegalArgumentException("Subject duration must be between 40 and 80 hours");
        this.duration = duration;
    }

    private void setSemester(int semester) {
        if (semester < 1) throw new IllegalArgumentException("Semester cannot be less than 1");
        this.semester = semester;
    }

    public void setRequired(boolean isRequired) { this.isRequired = isRequired; }

}
