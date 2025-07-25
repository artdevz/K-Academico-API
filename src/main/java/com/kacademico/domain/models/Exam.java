package com.kacademico.domain.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Exam {

    private static final int MAX_NAME_LENGHT = 16;
    private static final int MIN_NAME_LENGHT = 3;
    private static final int MAX_MAXIMUM = 10;
    private static final int MIN_MAXIMUM = 0;

    private UUID id;
    private String name;
    private int maximum;
    private LocalDate date;

    private Grade grade;

    private Set<Evaluation> evaluations = new HashSet<>();

    public Exam() {};

    public Exam(UUID id, String name, int maximum, LocalDate date, Grade grade) {
        this.id = id;
        setName(name);
        setMaximum(maximum);
        this.date = date;
        this.grade = grade;
    }
    
    public UUID getId() { return id; }
    public String getName() { return name; }
    public int getMaximum() { return maximum; }
    public LocalDate getDate() { return date; }
    public Grade getGrade() { return grade; }
    public Set<Evaluation> getEvaluations() { return evaluations; }

    public void setName(String name) {
        if (name.length() < MIN_NAME_LENGHT || name.length() > MAX_NAME_LENGHT) throw new IllegalArgumentException("Name must be between " + MIN_NAME_LENGHT + " and " + MAX_NAME_LENGHT + "characters");
        this.name = name;
    }

    public void setMaximum(int maximum) {
        if (maximum < MIN_MAXIMUM || maximum > MAX_MAXIMUM) throw new IllegalArgumentException("Maximum value must be between " + MIN_MAXIMUM + " and " + MAX_MAXIMUM);
        this.maximum = maximum;
    }

}
