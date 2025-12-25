package kz.soft.finalrpo.dto.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseUpdateRequest {
    @NotBlank
    private String title;
    private String description;
}
