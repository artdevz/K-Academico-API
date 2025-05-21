package com.kacademico.domain.models;

import java.util.Set;
import java.util.UUID;

public class User {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    // private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,32}$";
    private static final int MAX_NAME_LENGTH = 48;
    private static final int MIN_NAME_LENGTH = 3;

    private UUID id;
    private String name;
    private String email;
    private String password;

    private Set<Role> roles;

    public User() {}

    public User(UUID id, String name, String email, String password, Set<Role> roles) {
        this.id = id;
        setName(name);
        setEmail(email);
        setPassword(password);
        this.roles = roles;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Set<Role> getRoles() { return roles; }

    public void setName(String name) { 
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) throw new IllegalArgumentException("Name must be between " + MIN_NAME_LENGTH + " and " + MAX_NAME_LENGTH + " characters");
        this.name = name; 
    }

    public void setEmail(String email) {
        validateEmail(email); 
        this.email = email; 
    }

    public void setPassword(String password) {
        // validatePassword(password);
        this.password = password; 
    }

    public void setRoles(Set<Role> roles) { this.roles = roles; }

    private void validateEmail(String email) { if ( !(email.matches(EMAIL_REGEX)) ) throw new IllegalArgumentException("Must be a well-formed email address"); }

    /*
    private void validatePassword(String password) {
        if ( !(password.matches(PASSWORD_REGEX)) ) throw new IllegalArgumentException("The password must contain at least one lowercase letter, one uppercase letter, one number, one special character, and be between 8 and 32 characters");
    }
    */
    
}