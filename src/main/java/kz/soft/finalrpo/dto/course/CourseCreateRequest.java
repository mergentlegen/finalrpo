package kz.soft.finalrpo.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseCreateRequest {
    @NotBlank
    private String title;
    private String description;

    @NotNull
    private Long teacherId;
}
