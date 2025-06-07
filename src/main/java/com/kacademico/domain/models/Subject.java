package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Subject {

    private static final int MAX_NAME_LENGTH = 128;
    private static final int MIN_NAME_LENGTH = 8;
    private static final int MAX_DESCRIPTION_LENGTH = 256;
    private static final int MAX_DURATION = 80;
    private static final int MIN_DURATION = 40;
    private static final int MIN_SEMESTER = 1;

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
        setCourse(course);
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
        if (name == null || name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) throw new IllegalArgumentException("Name must be between " + MIN_NAME_LENGTH + "and" + MAX_NAME_LENGTH + " characters"); 
        this.name = name;
    }

    public void setDescription(String description) {
        if (description == null || description.length() > MAX_DESCRIPTION_LENGTH) throw new IllegalArgumentException("Description must be at most " + MAX_DESCRIPTION_LENGTH + " characters"); 
        this.description = description;
    }

    private void setDuration(int duration) {
        if (duration < MIN_DURATION || duration > MAX_DURATION) throw new IllegalArgumentException("Subject duration must be between " + MIN_DURATION + " and " + MAX_DURATION + " hours");
        this.duration = duration;
    }

    private void setSemester(int semester) {
        if (semester < MIN_SEMESTER) throw new IllegalArgumentException("Semester cannot be less than " + MIN_SEMESTER);
        this.semester = semester;
    }

    public void setRequired(boolean isRequired) { this.isRequired = isRequired; }
    public void setCourse(Course course) { this.course = course; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id != null && id.equals(subject.id);
    }

}
