package com.kacademico.domain.models;

import java.util.UUID;

public class Role {

    private static final int MAX_NAME_LENGTH = 20;
    private static final int MIN_NAME_LENGTH = 4;
    private static final int MAX_DESCRIPTION_LENGTH = 255;

    private UUID id;
    private String name;
    private String description;

    public Role() {};

    public Role(UUID id, String name, String description) {
        this.id = id;
        setName(name);
        setDesciption(description);
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setName(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) throw new IllegalArgumentException("Role name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters");
        this.name = name;
    }

    public void setDesciption(String description) {
        if (description.length() > MAX_DESCRIPTION_LENGTH) throw new IllegalArgumentException("Role Description cannot exceed " + MAX_DESCRIPTION_LENGTH + "characters");
        this.description = description;
    }

    /*
     * ADMIN
     * PROFESSOR
     * STUDENT
     * GUEST
     */

}