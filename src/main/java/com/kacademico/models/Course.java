package com.kacademico.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @NotBlank(message="Nome do Curso não deve está em branco.")
    @Size(min=4, max=160, message="Nome do Curso deve conter entre 4 e 160 caractéres.")
    private String name;

    @Min(40)
    private int duration; // In Hours.

    @Size(max=256)
    private String description;

    public Course(String name, int duration, String description) {
        this.name = name;
        this.duration = duration;
        this.description = description;
    }

}
