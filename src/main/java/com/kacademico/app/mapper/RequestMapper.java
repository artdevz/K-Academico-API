package com.kacademico.app.mapper;

// import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.kacademico.app.dto.attendance.AttendanceRequestDTO;
import com.kacademico.app.dto.course.CourseRequestDTO;
import com.kacademico.app.dto.course.WorkloadDTO;
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
import com.kacademico.domain.enums.EEnrollee;
import com.kacademico.domain.enums.EGrade;
import com.kacademico.domain.enums.ELesson;
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
import com.kacademico.domain.models.values.Workload;
import com.kacademico.domain.repositories.IEnrolleeRepository;
import com.kacademico.domain.repositories.IExamRepository;
import com.kacademico.domain.repositories.IGradeRepository;
import com.kacademico.domain.repositories.ICourseRepository;
import com.kacademico.domain.repositories.ILessonRepository;
import com.kacademico.domain.repositories.IProfessorRepository;
import com.kacademico.domain.repositories.IRoleRepository;
import com.kacademico.domain.repositories.IStudentRepository;
import com.kacademico.domain.repositories.ISubjectRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RequestMapper {

    private final EntityFinder finder;

    private final ICourseRepository courseR;
    private final ISubjectRepository subjectR;
    private final IStudentRepository studentR;
    private final IProfessorRepository professorR;
    private final IGradeRepository gradeR;
    private final IEnrolleeRepository enrolleeR;
    private final IExamRepository examR;
    private final ILessonRepository lessonR;
    private final IRoleRepository roleR;
    
    private final EnrollmentGeneratorService enrollmentGS;

    public Course toCourse(CourseRequestDTO data) {
        return new Course(
            null,
            data.name(),
            data.code(),
            data.description(),
            toWorkload(data.workload())
        );
    }

    public Workload toWorkload(WorkloadDTO data) {
        return new Workload(
            0, // Default Value, Será aumentado ou diminuido a medida que Subjects são criadas
            data.electiveHours(),
            data.complementaryHours(),
            data.internshipHours()
        );
    }

    public User toUser(UserRequestDTO data) {
        return new User(
            null,
            data.name(),
            data.email(),
            data.password(),
            findRoles(data.roles())
        );
    }

    public Subject toSubject(SubjectRequestDTO data) {
        return new Subject(
            null,
            data.name(),
            data.description(),
            data.duration(),
            data.semester(),
            data.isRequired(),
            finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found")
            // findEquivalences(data.prerequisites())
        );
    }

    public Student toStudent(StudentRequestDTO data) {
        return new Student(
            null,
            data.user().name(),
            data.user().email(),
            data.user().password(),
            findRoles(data.user().roles()),
            0, // DEFAULT CREDITS STARTER VALUE
            0, // DEFAULT AVERAGE STARTER VALUE
            enrollmentGS.generate(finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found").getCode()),
            finder.findByIdOrThrow(courseR.findById(data.course()), "Course not Found")
        );
    }

    public Professor toProfessor(ProfessorRequestDTO data) {
        return new Professor(
            null,
            data.user().name(),
            data.user().email(),
            data.user().password(),
            findRoles(data.user().roles())
        );
    }

    public Grade toGrade(GradeRequestDTO data) {
        return new Grade(
            null,
            data.capacity(),
            0, // DEFAULT CURRENT STUDENTS STARTER VALUE
            EGrade.PENDING, // DEFAULT GRADE STATUS STARTER VALUE
            data.schedule(),
            data.timetable(),
            finder.findByIdOrThrow(subjectR.findById(data.subject()), "Grade not Found"),
            finder.findByIdOrThrow(professorR.findById(data.professor()), "Professor not Found")
        );
    }

    public Enrollee toEnrollee(EnrolleeRequestDTO data) {
        return new Enrollee(
            null,
            0, // DEFAULT ABSENCES STARTER VALUE
            0f, // DEFAULT AVERAGE STARTER VALUE
            EEnrollee.ENROLLED, // DEFAULT ENROLLEE STATUS STARTER VALUE
            finder.findByIdOrThrow(gradeR.findById(data.grade()), "Grade not Found"),
            finder.findByIdOrThrow(studentR.findById(data.student()), "Student not Found")
        );
    }

    public Exam toExam(ExamRequestDTO data) {
        return new Exam(
            null,
            data.name(),
            data.maximum(),
            data.date(),
            finder.findByIdOrThrow(gradeR.findById(data.grade()), "Grade not Found")
        );
    }

    public Evaluation toEvaluation(EvaluationRequestDTO data) {
        return new Evaluation(
            null,
            data.score(),
            finder.findByIdOrThrow(enrolleeR.findById(data.enrollee()), "Enrollee not Found"),
            finder.findByIdOrThrow(examR.findById(data.exam()), "Exam not Found")
        );
    }

    public Lesson toLesson(LessonRequestDTO data) {
        return new Lesson(
            null,
            data.topic(),
            data.date(),
            ELesson.UPCOMING, // DEFAULT LESSON STATUS STARTER VALUE
            finder.findByIdOrThrow(gradeR.findById(data.grade()), "Grade not Found")
        );
    }

    public Attendance toAttendance(AttendanceRequestDTO data) {
        return new Attendance(
            null,
            data.isAbsent(),
            finder.findByIdOrThrow(enrolleeR.findById(data.enrollee()), "Enrollee not Found"),
            finder.findByIdOrThrow(lessonR.findById(data.lesson()), "Lesson not Found")
        );
    }

    public Equivalence toEquivalence(EquivalenceRequestDTO data) {
        return new Equivalence(
            null,
            data.name()
        );
    }

    /*
    private List<Equivalence> findEquivalences(List<UUID> equivalences) {
        return equivalences.stream()
            .map(id -> finder.findByIdOrThrow(equivalenceR.findById(id), "Equivalence not Found"))
        .collect(Collectors.toList());
    }*/

    private Set<Role> findRoles(Set<UUID> roles) {
        return roles.stream()
        .map(roleId -> roleR.findById(roleId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not Found")))
        .collect(Collectors.toSet());
    }

}
