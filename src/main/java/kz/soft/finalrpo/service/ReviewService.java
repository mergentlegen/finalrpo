package kz.soft.finalrpo.service;

import jakarta.persistence.EntityNotFoundException;
import kz.soft.finalrpo.dto.review.ReviewCreateRequest;
import kz.soft.finalrpo.dto.review.ReviewUpdateRequest;
import kz.soft.finalrpo.dto.review.ReviewResponse;
import kz.soft.finalrpo.entity.Course;
import kz.soft.finalrpo.entity.Review;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.ReviewMapper;
import kz.soft.finalrpo.repository.CourseRepository;
import kz.soft.finalrpo.repository.EnrollmentRepository;
import kz.soft.finalrpo.repository.ReviewRepository;
import kz.soft.finalrpo.repository.UserRepository;
import kz.soft.finalrpo.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ReviewMapper reviewMapper;
    private final UserService userService;

    public ReviewResponse create(CustomUserDetails currentUser, ReviewCreateRequest request) {

        // ✅ Получаем курс
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + request.getCourseId()));

        // ✅ Получаем автора
        User author = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + currentUser.getId()));

        // ✅ ШАГ 6.1: отзыв может оставить только записанный студент
        boolean enrolled = enrollmentRepository.existsByStudentIdAndCourseId(author.getId(), course.getId());
        if (!enrolled) {
            throw new IllegalArgumentException("You must be enrolled in the course to leave a review");
        }

        // ✅ ШАГ 6.2: один отзыв на курс от одного пользователя
        boolean alreadyReviewed = reviewRepository.existsByAuthorIdAndCourseId(author.getId(), course.getId());
        if (alreadyReviewed) {
            throw new IllegalArgumentException("You already left a review for this course");
        }

        // ✅ Создаём отзыв
        Review review = Review.builder()
                .course(course)
                .author(author)
                .rating(request.getRating())
                .text(request.getText())
                .createdAt(LocalDateTime.now())
                .build();

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    public List<ReviewResponse> getByCourse(Long courseId) {
        return reviewRepository.findAllByCourseId(courseId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    public ReviewResponse update(CustomUserDetails currentUser, Long reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + reviewId));

        if (!review.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can update only your review");
        }

        review.setRating(request.getRating());
        review.setText(request.getText());

        return reviewMapper.toResponse(reviewRepository.save(review));
    }

    public void delete(CustomUserDetails currentUser, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + reviewId));

        if (!review.getAuthor().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You can delete only your review");
        }

        reviewRepository.delete(review);
    }
}
