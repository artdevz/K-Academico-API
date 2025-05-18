package com.kacademico.infra.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Entity
@Table(name = "professors")
public class ProfessorEntity extends UserEntity {
    
    @OneToMany(mappedBy = "professor", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<GradeEntity> grades = new ArrayList<>();

}
