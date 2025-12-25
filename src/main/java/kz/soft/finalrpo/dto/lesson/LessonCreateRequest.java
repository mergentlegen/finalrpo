package kz.soft.finalrpo.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LessonCreateRequest {
    @NotBlank
    private String title;
    private String content;

    @NotNull
    private Long courseId;
}
