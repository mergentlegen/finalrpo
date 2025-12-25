package kz.soft.finalrpo.service;

import kz.soft.finalrpo.dto.enrollment.EnrollmentCreateRequest;
import kz.soft.finalrpo.dto.enrollment.EnrollmentResponse;
import kz.soft.finalrpo.entity.Course;
import kz.soft.finalrpo.entity.Enrollment;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.EnrollmentMapper;
import kz.soft.finalrpo.repository.CourseRepository;
import kz.soft.finalrpo.repository.EnrollmentRepository;
import kz.soft.finalrpo.repository.UserRepository;
import kz.soft.finalrpo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentMapper enrollmentMapper;

    // STUDENT записывается на курс
    public EnrollmentResponse enroll(CustomUserDetails currentUser, EnrollmentCreateRequest request) {

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        User student = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (enrollmentRepository.existsByCourseIdAndStudentId(course.getId(), student.getId())) {
            throw new RuntimeException("You already enrolled to this course!");
        }

        Enrollment enrollment = Enrollment.builder()
                .course(course)
                .student(student)
                .enrolledAt(LocalDateTime.now())
                .build();

        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    // STUDENT видит свои записи
    public List<EnrollmentResponse> myEnrollments(CustomUserDetails currentUser) {
        return enrollmentRepository.findAllByStudentId(currentUser.getId())
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    // TEACHER/ADMIN видит студентов на курсе
    public List<EnrollmentResponse> getByCourse(Long courseId) {
        return enrollmentRepository.findAllByCourseId(courseId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    // STUDENT может отписаться от курса
    public void cancel(CustomUserDetails currentUser, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByCourseIdAndStudentId(courseId, currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollmentRepository.delete(enrollment);
    }
}
