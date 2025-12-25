package kz.soft.finalrpo.dto.enrollment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnrollmentCreateRequest {

    @NotNull
    private Long courseId;
}
