package kz.soft.finalrpo.controller;

import jakarta.validation.Valid;
import kz.soft.finalrpo.dto.review.ReviewCreateRequest;
import kz.soft.finalrpo.dto.review.ReviewResponse;
import kz.soft.finalrpo.dto.review.ReviewUpdateRequest;
import kz.soft.finalrpo.security.CustomUserDetails;
import kz.soft.finalrpo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @PostMapping
    public ReviewResponse create(@AuthenticationPrincipal CustomUserDetails currentUser,
                                 @RequestBody @Valid ReviewCreateRequest request) {
        return reviewService.create(currentUser, request);
    }

    @GetMapping("/course/{courseId}")
    public List<ReviewResponse> getByCourse(@PathVariable Long courseId) {
        return reviewService.getByCourse(courseId);
    }

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @PutMapping("/{reviewId}")
    public ReviewResponse update(@AuthenticationPrincipal CustomUserDetails currentUser,
                                 @PathVariable Long reviewId,
                                 @RequestBody @Valid ReviewUpdateRequest request) {
        return reviewService.update(currentUser, reviewId, request);
    }

    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    @DeleteMapping("/{reviewId}")
    public String delete(@AuthenticationPrincipal CustomUserDetails currentUser,
                         @PathVariable Long reviewId) {
        reviewService.delete(currentUser, reviewId);
        return "Review deleted!";
    }
}
