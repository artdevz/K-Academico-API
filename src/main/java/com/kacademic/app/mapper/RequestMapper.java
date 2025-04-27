package com.kacademic.app.mapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.kacademic.app.dto.attendance.AttendanceRequestDTO;
import com.kacademic.app.dto.course.CourseRequestDTO;
import com.kacademic.app.dto.enrollee.EnrolleeRequestDTO;
import com.kacademic.app.dto.equivalence.EquivalenceRequestDTO;
import com.kacademic.app.dto.evaluation.EvaluationRequestDTO;
import com.kacademic.app.dto.exam.ExamRequestDTO;
import com.kacademic.app.dto.grade.GradeRequestDTO;
import com.kacademic.app.dto.lesson.LessonRequestDTO;
import com.kacademic.app.dto.professor.ProfessorRequestDTO;
import com.kacademic.app.dto.student.StudentRequestDTO;
import com.kacademic.app.dto.subject.SubjectRequestDTO;
import com.kacademic.app.dto.user.UserRequestDTO;
import com.kacademic.app.helpers.EntityFinder;
import com.kacademic.app.services.EnrollmentGeneratorService;
import com.kacademic.domain.models.Attendance;
import com.kacademic.domain.models.Course;
import com.kacademic.domain.models.Enrollee;
import com.kacademic.domain.models.Equivalence;
import com.kacademic.domain.models.Evaluation;
import com.kacademic.domain.models.Exam;
import com.kacademic.domain.models.Grade;
import com.kacademic.domain.models.Lesson;
import com.kacademic.domain.models.Professor;
import com.kacademic.domain.models.Role;
import com.kacademic.domain.models.Student;
import com.kacademic.domain.models.Subject;
import com.kacademic.domain.models.User;
import com.kacademic.domain.repositories.CourseRepository;
import com.kacademic.domain.repositories.EnrolleeRepository;
import com.kacademic.domain.repositories.EquivalenceRepository;
import com.kacademic.domain.repositories.ExamRepository;
import com.kacademic.domain.repositories.GradeRepository;
import com.kacademic.domain.repositories.LessonRepository;
import com.kacademic.domain.repositories.ProfessorRepository;
import com.kacademic.domain.repositories.RoleRepository;
import com.kacademic.domain.repositories.StudentRepository;
import com.kacademic.domain.repositories.SubjectRepository;

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
            data.semester(),
            data.locate(),
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
