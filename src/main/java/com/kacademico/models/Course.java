package com.kacademico.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name="courses")
@Entity
public class Course implements Serializable {
    
    private static final long serialVersionUID = 1L;

    // Identifier
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Relationships
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Subject> subjects = new ArrayList<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Student> students = new ArrayList<>();

    // Simple Attributes
    @NotBlank(message="Nome do Curso não deve está em branco.")
    @Size(min=4, max=160, message="Nome do Curso deve conter entre 4 e 160 caractéres.")
    private String name;

    @Pattern(regexp = "^\\d+$", message = "O Código do Curso deve ser composto apenas por números.")
    @Size(min=2, max=2, message="O Código do Curso deve conter apenas 2 caractéres.")
    private String code;
    
    private int duration; // In Hours.

    @Size(max=256)
    private String description;

    // Constructor
    public Course(String name, String code, int duration, String description) {
        this.name = name;
        this.code = code;
        this.duration = duration;
        this.description = description;
    }

}
