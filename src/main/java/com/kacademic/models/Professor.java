package com.kacademic.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "professors")
@Entity
public class Professor implements Serializable {
 
    private static final long serialVersionUID = 1L;

    // Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relationships
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Grade> grades = new ArrayList<>();

    // Simple Attributes
    private int wage; // Wage in cents (e.g., 1000 cents = 10.00)

    // Constructor
    public Professor(User user, int wage) {
        this.user = user;
        this.wage = wage;
    }

}
