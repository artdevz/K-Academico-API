package com.kacademico.infra.embeddables;

import java.util.Objects;

import com.kacademico.domain.models.values.Enrollment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class EnrollmentEmbeddable {
    
    @Column(name = "enrollment", nullable = false, unique = true, length = 11)
    private String value;

    public EnrollmentEmbeddable(String value) {
        if (!value.matches("\\d{3}\\.\\d{3}\\.\\d{3}")) throw new IllegalArgumentException("Enrollment must match pattern: YYS.CCC.RRR (e.g., 251.505.123)");
        this.value = value;
    }

    public String getYear() { return value.substring(0, 2); }
    public String getSemester() { return value.substring(0, 3); }
    public String getCourseCode() { return value.substring(4, 7); }
    public String getRandomCode() { return value.substring(8, 11); }
    public String getValue() { return value; }

    public Enrollment toDomain() { return new Enrollment(this.value); }
    public static EnrollmentEmbeddable fromDomain(Enrollment enrollment) { return new EnrollmentEmbeddable(enrollment.getValue()); }

    @Override
    public String toString() { return value; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrollmentEmbeddable)) return false;
        EnrollmentEmbeddable that = (EnrollmentEmbeddable) o;
        return Objects.equals(value, that.value);
    }

}
