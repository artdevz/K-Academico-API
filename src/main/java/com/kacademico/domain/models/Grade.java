package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.models.values.Schedule;
import com.kacademico.domain.models.values.Timetable;

public class Grade {

    private static final int MIN_CAPACITY = 20;
    private static final int MIN_CURRENT_STUDENTS = 0;
    
    private UUID id;
    private int capacity;
    private int currentStudents;
    private EGrade status; // Grade Status (PENDING, ONGOING, FINAL, FINISHED)

    private Schedule schedule;

    private List<Timetable> timetables = new ArrayList<>();

    private Subject subject;
    private Professor professor;

    private Set<Enrollee> enrollees = new HashSet<>();
    private List<Exam> exams = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();

    public Grade() {};

    public Grade(UUID id, int capacity, int currentStudents, EGrade status, Schedule schedule, List<Timetable> timetables, Subject subject, Professor professor) {
        this.id = id;
        setCapacity(capacity);
        setCurrentStudents(currentStudents);
        setStatus(status);
        setSchedule(schedule);
        setTimetable(timetables);
        this.subject = subject;
        setProfessor(professor);
    }
    
    public UUID getId() { return id; }
    public int getCapacity() { return capacity; }
    public int getCurrentStudents() { return currentStudents; }
    public EGrade getStatus() { return status; }
    public Schedule getSchedule() { return schedule; }
    public List<Timetable> getTimetables() { return timetables; }
    public Subject getSubject() { return subject; }
    public Professor getProfessor() { return professor; }
    public Set<Enrollee> getEnrollees() { return enrollees; }
    public List<Exam> getExams() { return exams; }
    public List<Lesson> getLessons() { return lessons; }

    public void setCapacity(int capacity) {
        if (capacity < MIN_CAPACITY) throw new IllegalArgumentException("Capacity cannot be less than " + MIN_CAPACITY);
        this.capacity = capacity;
    }

    public void setCurrentStudents(int currentStudents) {
        if (this.status == EGrade.FINISHED) throw new IllegalArgumentException("Cannot update a grade that is already finished");
        if (currentStudents < MIN_CURRENT_STUDENTS) throw new IllegalArgumentException("Current Students cannot be negative");
        if (currentStudents > this.capacity) throw new IllegalArgumentException("Cannot enroll more students: maximum capacity has been reached: " + this.capacity);
        this.currentStudents = currentStudents;
    }

    public void setStatus(EGrade status) {
        this.setStatus(status);
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setTimetable(List<Timetable> timetables) {
        this.timetables = timetables;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

}
