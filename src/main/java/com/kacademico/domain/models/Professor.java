package com.kacademico.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "professors")
@Entity
@DiscriminatorValue("PROFESSOR")
public class Professor extends User {

    @OneToMany(mappedBy = "professor", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Grade> grades = new ArrayList<>();

    public Professor(String name, String email, String password, Set<Role> roles) {
        super(name, email, password, roles);
    }

}