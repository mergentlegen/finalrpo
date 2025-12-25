package kz.soft.finalrpo.dto.course;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Long teacherId;
    private String teacherEmail;
}
