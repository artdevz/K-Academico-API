package com.kacademico.domain.models;

import java.util.Date;
import java.util.UUID;

public class Token {
    
    private UUID id;
    private String token;
    private Date expiryDate;

    private User user;

    public Token() {};

    public Token(UUID id, String token, Date expiryDate, User user) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public UUID getId() { return this.id; }
    public String getToken() { return this.token; }
    public Date getExpiryDate() { return this.expiryDate; }
    public User getUser() { return this.user; }

}
