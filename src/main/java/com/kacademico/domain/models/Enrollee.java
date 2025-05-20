package com.kacademico.domain.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.enums.EEnrollee;
import com.kacademico.domain.enums.EGrade;

public class Enrollee {

    private static final float MAX_PERCENTAGEM_ABSENCES = 0.25f;
    private static final float MAX_AVERAGE = 10f;
    private static final float MIN_AVERAGE = 0f;
    private static final int MIN_ABSENCES = 0;
        
    private UUID id;
    private int absences;
    private float average;
    private EEnrollee status; // Student Status (Enrolled, APPROVED, FINAL_EXAM, FAILED) 

    private Grade grade;
    private Student student;

    private Set<Evaluation> evaluations = new HashSet<>();
    private Set<Attendance> attendances = new HashSet<>();

    public Enrollee() {};

    public Enrollee(UUID id, int absences, float average, EEnrollee status, Grade grade, Student student) {
        this.id = id;
        setAbsences(absences);
        setAverage(average);
        setStatus(status);
        setGrade(grade);
        this.student = student;
    }

    public UUID getId() { return id; }
    public int getAbsences() { return absences; }
    public float getAverage() { return average; }
    public EEnrollee getStatus() { return status; }
    public Grade getGrade() { return grade; }
    public Student getStudent() { return student; }
    public Set<Evaluation> getEvaluations() { return evaluations; }
    public Set<Attendance> getAttendances() { return attendances; }

    public void setAbsences(int absences) {
        if (absences < MIN_ABSENCES) throw new IllegalArgumentException("Absences cannot be negative");
        this.absences = absences;
        updateStatusByAbsences();        
    }

    public void setAverage(float average) {
        if (average < MIN_AVERAGE || average > MAX_AVERAGE) throw new IllegalArgumentException("Average must be between " + MIN_AVERAGE + " and " + MAX_AVERAGE);
        this.average = average;
    }

    public void setStatus(EEnrollee status) {
        this.status = status;
    }

    public void setGrade(Grade grade) { // 422
        if (grade.getStatus() == EGrade.FINISHED) throw new IllegalArgumentException("Cannot enroll a student in a finished grade");
    }

    private void updateStatusByAbsences() {
        int maxAbsences = this.grade.getSubject().getDuration();
        if (this.absences > (int) Math.floor(maxAbsences * MAX_PERCENTAGEM_ABSENCES)) this.status = EEnrollee.FAILED;
    }

}
