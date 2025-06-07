package com.kacademico.infra.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Course")
@Table(name = "courses")
public class CourseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    private int duration;

    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE, orphanRemoval = false)
    private List<StudentEntity> students = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.MERGE, orphanRemoval = false)
    private List<SubjectEntity> subjects = new ArrayList<>();

}
