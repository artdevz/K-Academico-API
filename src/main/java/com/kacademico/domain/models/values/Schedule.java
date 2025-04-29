package com.kacademico.domain.models.values;

import java.util.Objects;

import com.kacademico.shared.utils.Semester;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Schedule {
    
    @Semester
    private String semester;

    private String locate;

    // private List<Timetable> timetable;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Schedule)) return false;
        Schedule that = (Schedule) o;
        return (
            Objects.equals(semester, that.semester) &&
            Objects.equals(locate, that.locate) //&&
            // Objects.equals(timetable, that.timetable)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(semester, locate);//, timetable);
    }

}
