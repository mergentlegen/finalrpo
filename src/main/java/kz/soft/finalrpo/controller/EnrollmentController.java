package kz.soft.finalrpo.controller;

import jakarta.validation.Valid;
import kz.soft.finalrpo.dto.enrollment.EnrollmentCreateRequest;
import kz.soft.finalrpo.dto.enrollment.EnrollmentResponse;
import kz.soft.finalrpo.security.CustomUserDetails;
import kz.soft.finalrpo.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // STUDENT: записаться на курс
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @PostMapping
    public EnrollmentResponse enroll(@AuthenticationPrincipal CustomUserDetails currentUser,
                                     @RequestBody @Valid EnrollmentCreateRequest request) {
        return enrollmentService.enroll(currentUser, request);
    }

    // STUDENT: мои курсы
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @GetMapping("/my")
    public List<EnrollmentResponse> myEnrollments(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return enrollmentService.myEnrollments(currentUser);
    }

    // TEACHER/ADMIN: список студентов на курсе
    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER','ROLE_ADMIN')")
    @GetMapping("/course/{courseId}")
    public List<EnrollmentResponse> byCourse(@PathVariable Long courseId) {
        return enrollmentService.getByCourse(courseId);
    }

    // STUDENT: отписаться
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @DeleteMapping("/course/{courseId}")
    public String cancel(@AuthenticationPrincipal CustomUserDetails currentUser,
                         @PathVariable Long courseId) {
        enrollmentService.cancel(currentUser, courseId);
        return "Enrollment cancelled!";
    }
}
