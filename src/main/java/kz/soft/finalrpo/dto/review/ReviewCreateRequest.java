package kz.soft.finalrpo.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewCreateRequest {

    @NotNull
    private Long courseId;

    @Min(1) @Max(5)
    private int rating;

    @NotBlank
    private String text;
}
