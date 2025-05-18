package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Equivalence {

    private UUID id;
    private String name;

    private List<Subject> subjects = new ArrayList<>();

    public Equivalence() {};

    public Equivalence(UUID id, String name, List<Subject> subjects) {
        this.id = id;
        setName(name);
        this.subjects = subjects;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public List<Subject> getSubjects() { return subjects; }

    public void setName(String name) { this.name = name; }
    public void setSubjects(List<Subject> subjects) { this.subjects = subjects; }

}
