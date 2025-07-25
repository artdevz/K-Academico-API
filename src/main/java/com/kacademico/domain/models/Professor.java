package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Professor extends User {

    private List<Grade> grades = new ArrayList<>();

    public Professor() {}

    public Professor(UUID id, String name, String email, String password, Set<Role> roles) {
        super(id, name, email, password, roles);
    }

    public List<Grade> getGrades() { return grades; }

}