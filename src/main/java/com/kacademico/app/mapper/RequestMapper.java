package com.kacademico.app.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.app.dto.attendance.AttendanceRequestDTO;
import com.kacademico.app.dto.course.CourseRequestDTO;
import com.kacademico.app.dto.enrollee.EnrolleeRequestDTO;
import com.kacademico.app.dto.equivalence.EquivalenceRequestDTO;
import com.kacademico.app.dto.evaluation.EvaluationRequestDTO;
import com.kacademico.app.dto.exam.ExamRequestDTO;
import com.kacademico.app.dto.grade.GradeRequestDTO;
import com.kacademico.app.dto.lesson.LessonRequestDTO;
import com.kacademico.app.dto.professor.ProfessorRequestDTO;
import com.kacademico.app.dto.student.StudentRequestDTO;
import com.kacademico.app.dto.subject.SubjectRequestDTO;
import com.kacademico.app.dto.user.UserRequestDTO;
import com.kacademico.app.helpers.EntityFinder;
import com.kacademico.app.services.EnrollmentGeneratorService;
import com.kacademico.domain.models.Attendance;
import com.kacademico.domain.models.Course;
import com.kacademico.domain.models.Enrollee;
import com.kacademico.domain.models.Equivalence;
import com.kacademico.domain.models.Evaluation;
import com.kacademico.domain.models.Exam;
import com.kacademico.domain.models.Grade;
import com.kacademico.domain.models.Lesson;
import com.kacademico.domain.models.Professor;
import com.kacademico.domain.models.Role;
import com.kacademico.domain.models.Student;
import com.kacademico.domain.models.Subject;
import com.kacademico.domain.models.User;
import com.kacademico.domain.repositories.CourseRepository;
import com.kacademico.domain.repositories.EnrolleeRepository;
import com.kacademico.domain.repositories.EquivalenceRepository;
import com.kacademico.domain.repositories.ExamRepository;
import com.kacademico.domain.repositories.GradeRepository;
import com.kacademico.domain.repositories.LessonRepository;
import com.kacademico.domain.repositories.ProfessorRepository;
import com.kacademico.domain.repositories.RoleRepository;
import com.kacademico.domain.repositories.StudentRepository;
import com.kacademico.domain.repositories.SubjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RequestMapper {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EntityFinder finder;

    private final CourseRepository courseR;
    private final SubjectRepository subjectR;
    private final StudentRepository studentR;
    private final ProfessorRepository professorR;
    private final GradeRepository gradeR;
    private final EnrolleeRepository enrolleeR;
    private final ExamRepository examR;
    private final LessonRepository lessonR;
    private final EquivalenceRepository equivalenceR;
    private final RoleRepository roleR;
    
    private final EnrollmentGeneratorService enrollmentGS;

    public Course toCourse(CourseRequestDTO data) {
        return new Course(
            data.name(),
            data.code(),
            data.description()
        );
    }

    public User toUser(UserRequestDTO data) {
        return new User(
            data.name(),
            data.email(),
            passwordEncoder.encode(data.password()),
            findRoles(data.roles())
        );
    }

    public Subject toSubject(SubjectRequestDTO data) {
        return new Subject(
            data.name(),
            data.description(),
            data.duration(),
            data.semester(),
            data.isRequired(),
            finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found"),
            findEquivalences(data.prerequisites())
        );
    }

    public Student toStudent(StudentRequestDTO data) {
        return new Student(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),
            findRoles(data.user().roles()),
            enrollmentGS.generate(finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found").getCode()),
            finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found")
        );
    }

    public Professor toProfessor(ProfessorRequestDTO data) {
        return new Professor(
            data.user().name(),
            data.user().email(),
            passwordEncoder.encode(data.user().password()),
            findRoles(data.user().roles()),
            data.wage()
        );
    }

    public Grade toGrade(GradeRequestDTO data) {
        return new Grade(
            finder.findByIdOrThrow(subjectR.findById(data.subject()), "Grade not Found"),
            finder.findByIdOrThrow(professorR.findById(data.professor()), "Professor not Found"),
            data.capacity(),
            data.schedule(),
            data.timetable()
        );
    }

    public Enrollee toEnrollee(EnrolleeRequestDTO data) {
        return new Enrollee(
            finder.findByIdOrThrow(gradeR.findById(data.grade()), "Grade not Found"),
            finder.findByIdOrThrow(studentR.findById(data.student()), "Student not Found")
        );
    }

    public Exam toExam(ExamRequestDTO data) {
        return new Exam(
            data.name(),
            data.maximum(),
            data.date(),
            finder.findByIdOrThrow(gradeR.findById(data.grade()), "Grade not Found")
        );
    }

    public Evaluation toEvaluation(EvaluationRequestDTO data) {
        return new Evaluation(
            data.score(),
            finder.findByIdOrThrow(enrolleeR.findById(data.enrollee()), "Enrollee not Found"),
            finder.findByIdOrThrow(examR.findById(data.exam()), "Exam not Found")
        );
    }

    public Lesson toLesson(LessonRequestDTO data) {
        return new Lesson(
            data.topic(),
            data.date(),
            finder.findByIdOrThrow(gradeR.findById(data.grade()), "Grade not Found")
        );
    }

    public Attendance toAttendance(AttendanceRequestDTO data) {
        return new Attendance(
            data.isAbsent(),
            finder.findByIdOrThrow(enrolleeR.findById(data.enrollee()), "Enrollee not Found"),
            finder.findByIdOrThrow(lessonR.findById(data.lesson()), "Lesson not Found")
        );
    }

    public Equivalence toEquivalence(EquivalenceRequestDTO data) {
        return new Equivalence(
            data.name(),
            data.subjects().stream().map(id -> finder.findByIdOrThrow(subjectR.findById(id), "Subject not Found")).toList()
        );
    }

    private List<Equivalence> findEquivalences(List<UUID> equivalences) {
        return equivalences.stream()
            .map(id -> finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found"))
        .collect(Collectors.toList());
    }

    private Set<Role> findRoles(Set<UUID> roles) {
        return roles.stream()
        .map(roleId -> roleR.findById(roleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
        .collect(Collectors.toSet());
    }

}
