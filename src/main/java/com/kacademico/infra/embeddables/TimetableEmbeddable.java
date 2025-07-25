package com.kacademico.infra.embeddables;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kacademico.domain.models.values.Timetable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TimetableEmbeddable {
    
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    public Timetable toDomain() { return new Timetable(this.day, this.startTime, this.endTime); }
    public static TimetableEmbeddable fromDomain(Timetable timetable) { return new TimetableEmbeddable(timetable.getDay(), timetable.getStartTime(), timetable.getEndTime()); }

    public boolean conflictWith(TimetableEmbeddable other) {
        if (!this.day.equals(other.day)) return false;

        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimetableEmbeddable)) return false;
        TimetableEmbeddable that = (TimetableEmbeddable) o;
        return (day == that.day &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime)
        );
    }

    @Override
    public int hashCode() { return Objects.hash(day, startTime, endTime); }

}
