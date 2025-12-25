package kz.soft.finalrpo.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long courseId;
    private Long authorId;
    private String authorEmail;
    private int rating;
    private String text;
    private LocalDateTime createdAt;
}
