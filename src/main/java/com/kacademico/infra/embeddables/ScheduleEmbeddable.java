package com.kacademico.infra.embeddables;

import java.util.Objects;

import com.kacademico.domain.models.values.Schedule;
import com.kacademico.shared.utils.Semester;

import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ScheduleEmbeddable {
    
    @Semester
    private String semester;

    private String locate;

    public Schedule toDomain() { return new Schedule(this.semester, this.locate); }
    public static ScheduleEmbeddable fromDomain(Schedule schedule) { return new ScheduleEmbeddable(schedule.getSemester(), schedule.getLocate()); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleEmbeddable)) return false;
        ScheduleEmbeddable that = (ScheduleEmbeddable) o;
        return (
            Objects.equals(semester, that.semester) &&
            Objects.equals(locate, that.locate)
        );
    }

    @Override
    public int hashCode() { return Objects.hash(semester, locate); }

}
