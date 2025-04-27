package com.kacademico.domain.models.values;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
public class Enrollment {

    @Column(name = "enrollment", nullable = false, unique = true, length = 11)
    private String value;

    public Enrollment(String value) {
        if (!value.matches("\\d{3}\\.\\d{3}\\.\\d{3}")) throw new IllegalArgumentException("Enrollment must match pattern: YYS.CCC.RRR (e.g., 251.505.123)");
        this.value = value;
    }

    public String getYear() {
        return value.substring(0, 2);
    }

    public String getSemester() {
        return value.substring(0, 3);
    }

    public String getCourseCode() {
        return value.substring(4, 7);
    }

    public String getRandomCode() {
        return value.substring(8, 11);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment)) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(value, that.value);
    }
    
}
