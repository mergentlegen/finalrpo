package kz.soft.finalrpo.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LessonUpdateRequest {
    @NotBlank
    private String title;
    private String content;
}
