package com.kacademico.app.mapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.kacademico.app.dto.attendance.AttendanceResponseDTO;
import com.kacademico.app.dto.course.CourseResponseDTO;
import com.kacademico.app.dto.enrollee.EnrolleeResponseDTO;
import com.kacademico.app.dto.equivalence.EquivalenceResponseDTO;
import com.kacademico.app.dto.evaluation.EvaluationResponseDTO;
import com.kacademico.app.dto.exam.ExamResponseDTO;
import com.kacademico.app.dto.grade.GradeResponseDTO;
import com.kacademico.app.dto.lesson.LessonResponseDTO;
import com.kacademico.app.dto.professor.ProfessorResponseDTO;
import com.kacademico.app.dto.student.StudentResponseDTO;
import com.kacademico.app.dto.subject.SubjectResponseDTO;
import com.kacademico.app.dto.user.UserResponseDTO;
import com.kacademico.domain.models.Attendance;
import com.kacademico.domain.models.Course;
import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.models.Equivalence;
import com.kacademico.domain.models.Evaluation;
import com.kacademico.domain.models.Exam;
import com.kacademico.domain.models.Grade;
import com.kacademico.domain.models.Lesson;
import com.kacademico.domain.models.Professor;
import com.kacademico.domain.models.Student;
import com.kacademico.domain.models.Subject;
import com.kacademico.domain.models.User;

@Component
public class ResponseMapper {
    
    public <T, R> List<R> toResponseDTOList(List<T> entities, Function<T, R> mapper) {
        return entities.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    public CourseResponseDTO toCourseResponseDTO(Course course) {
        return new CourseResponseDTO(
            course.getId(),
            course.getName(),
            course.getCode(),
            course.getDuration(),
            course.getDescription()
        );
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRoles()
        );
    }

    public SubjectResponseDTO toSubjectResponseDTO(Subject subject) {
        return new SubjectResponseDTO(
            subject.getId(),
            subject.getCourse().getId(),
            subject.getName(),
            subject.getDescription(),
            subject.getDuration(),
            subject.getSemester(),
            subject.isRequired()
        );
    }

    public StudentResponseDTO toStudentResponseDTO(Student student) {
        return new StudentResponseDTO(
            student.getId(),
            student.getCourse().getId(),
            student.getEnrollment().getValue(),
            student.getName(),
            student.getEmail(),
            student.getAverage()
        );
    }

    public ProfessorResponseDTO toProfessorResponseDTO(Professor professor) {
        return new ProfessorResponseDTO(
            professor.getId(),
            professor.getName(),
            professor.getEmail(),
            professor.getWage()
        );
    }

    public GradeResponseDTO toGradeResponseDTO(Grade grade) {
        return new GradeResponseDTO(
            grade.getId(),
            grade.getSubject().getId(),
            grade.getProfessor().getId(),
            grade.getCapacity(),
            grade.getCurrentStudents(),
            grade.getSemester(),
            grade.getStatus(),
            grade.getLocate(),
            grade.getTimetables()
        );
    }

    public EnrolleeResponseDTO toEnrolleeResponseDTO(Enrollee enrollee) {
        return new EnrolleeResponseDTO(
            enrollee.getId(),
            enrollee.getStudent().getId(),
            enrollee.getGrade().getId(),
            enrollee.getAbsences(),
            enrollee.getAverage(),
            enrollee.getStatus()
        );
    }

    public ExamResponseDTO toExamResponseDTO(Exam exam) {
        return new ExamResponseDTO(
            exam.getId(), 
            exam.getGrade().getId(), 
            exam.getName(), 
            exam.getMaximum(), 
            exam.getDate()
        );
    }

    public EvaluationResponseDTO toEvaluationResponseDTO(Evaluation evaluation) {
        return new EvaluationResponseDTO(
            evaluation.getId(),
            evaluation.getEnrollee().getId(),
            evaluation.getExam().getGrade().getId(),
            evaluation.getExam().getId(),
            evaluation.getScore()
        );
    }

    public LessonResponseDTO toLessonResponseDTO(Lesson lesson) {
        return new LessonResponseDTO(
            lesson.getId(),
            lesson.getGrade().getId(),
            lesson.getTopic(),
            lesson.getDate(),
            lesson.getStatus()
        );
    }

    public AttendanceResponseDTO toAttendanceResponseDTO(Attendance attendance) {
        return new AttendanceResponseDTO(
            attendance.getId(),
            attendance.getEnrollee().getId(),
            attendance.getLesson().getId(),
            attendance.isAbsent()
        );
    }

    public EquivalenceResponseDTO toEquivalenceResponseDTO(Equivalence equivalence) {
        return new EquivalenceResponseDTO(
            equivalence.getId(),
            equivalence.getName()
        );
    }

}
