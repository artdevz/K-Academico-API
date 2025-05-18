package com.kacademico.domain.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.kacademico.domain.enums.ELesson;

public class Lesson {

    private static final int MAX_TOPIC_LENGTH = 32;

    private UUID id;
    private String topic;
    private LocalDate date;
    private ELesson status; // (UPCOMING, PENDING)

    private Grade grade;

    private Set<Attendance> attendances = new HashSet<>();

    public Lesson() {};

    public Lesson(UUID id, String topic, LocalDate date, ELesson status, Grade grade) {
        this.id = id;
        setTopic(topic);
        setDate(date);
        setStatus(status);
        this.grade = grade;
    }

    public UUID getId() { return id; }
    public String getTopic() { return topic; }
    public LocalDate getDate() { return date; }
    public ELesson getStatus() { return status; }
    public Grade getGrade() { return grade; }
    public Set<Attendance> getAttendances() { return attendances; }

    public void setTopic(String topic) {
        if (topic.length() > MAX_TOPIC_LENGTH) throw new IllegalArgumentException("Topic cannot be most than " + MAX_TOPIC_LENGTH + "characters");
        this.topic = topic;
    }

    public void setDate(LocalDate date) { this.date = date; }
    public void setStatus(ELesson status) { this.status = status; }

}
