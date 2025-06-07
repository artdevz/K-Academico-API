package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.models.values.Enrollment;

public class Student extends User {

    private static final int MIN_CREDITS = 0;
    private static final int MAX_AVERAGE = 10;
    private static final int MIN_AVERAGE = 0;
    
    private int credits;
    private Float average;

    private Enrollment enrollment;

    private Course course; 

    private List<Enrollee> enrollees = new ArrayList<>();

    public Student() {};
        
    public Student(UUID id, String name, String email, String password, Set<Role> roles, int credits, float average, Enrollment enrollment, Course course) {
        super(id, name, email, password, roles);
        setCredits(credits);
        setAverage(average);
        this.enrollment = enrollment;
        this.course = course;
    }

    public int getCredits() { return credits; }
    public float getAverage() { return average; }
    public Enrollment getEnrollment() { return enrollment; }
    public Course getCourse() { return course; }
    public List<Enrollee> getEnrollees() { return enrollees; }

    public void setCredits(int credits) {
        if (credits < MIN_CREDITS) throw new IllegalArgumentException("Credits cannot be negative");
        this.credits = credits;
    }

    public void setAverage(float average) {
        if (average < MIN_AVERAGE || average > MAX_AVERAGE) throw new IllegalArgumentException("Average must be between " + MIN_AVERAGE + " and " + MAX_AVERAGE);
        this.average = average;
    }

}