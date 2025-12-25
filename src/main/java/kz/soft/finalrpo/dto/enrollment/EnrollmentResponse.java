package kz.soft.finalrpo.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EnrollmentResponse {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private Long studentId;
    private String studentEmail;
    private LocalDateTime enrolledAt;
}
