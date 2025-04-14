package com.kacademic.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "equivalences")
@Entity
public class Equivalence {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @ManyToMany(mappedBy = "prerequisites", cascade = CascadeType.MERGE)
    private List<Subject> subjects = new ArrayList<>();

    public Equivalence(String name, List<Subject> subjects) {
        this.name = name;
        this.subjects = subjects;
    }

}
