package com.kacademico.domain.models.values;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class Timetable {
    
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Timetable)) return false;
        Timetable that = (Timetable) o;
        return (day == that.day &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startTime, endTime);
    }

}
