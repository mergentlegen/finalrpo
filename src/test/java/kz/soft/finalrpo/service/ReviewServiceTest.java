package kz.soft.finalrpo.service;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private UserService userService; // есть в сервисе, но не используется в методах — просто чтобы DI было

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void create_shouldSaveReviewAndReturnResponse() {
        // given
        Long courseId = 1L;
        Long userId = 7L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(userId);

        ReviewCreateRequest request = new ReviewCreateRequest();
        request.setCourseId(courseId);
        request.setRating(5);
        request.setText("Very good. Ағай поставьте 80% пожалуйста.");

        Course course = new Course();
        course.setId(courseId);

        User author = new User();
        author.setId(userId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(userRepository.findById(userId)).thenReturn(Optional.of(author));

        when(enrollmentRepository.existsByStudentIdAndCourseId(userId, courseId)).thenReturn(true);
        when(reviewRepository.existsByAuthorIdAndCourseId(userId, courseId)).thenReturn(false);

        Review saved = new Review();
        saved.setId(10L);
        saved.setCourse(course);
        saved.setAuthor(author);

        when(reviewRepository.save(any(Review.class))).thenReturn(saved);

        ReviewResponse mapped = mock(ReviewResponse.class);
        when(reviewMapper.toResponse(saved)).thenReturn(mapped);

        // when
        ReviewResponse result = reviewService.create(currentUser, request);

        // then
        assertNotNull(result);
        assertSame(mapped, result);

        verify(reviewRepository).save(any(Review.class));
        verify(reviewMapper).toResponse(saved);
    }

    @Test
    void create_shouldThrowIfNotEnrolled() {
        // given
        Long courseId = 1L;
        Long userId = 7L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(userId);

        ReviewCreateRequest request = new ReviewCreateRequest();
        request.setCourseId(courseId);
        request.setRating(5);
        request.setText("Text");

        Course course = new Course();
        course.setId(courseId);

        User author = new User();
        author.setId(userId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(userRepository.findById(userId)).thenReturn(Optional.of(author));

        when(enrollmentRepository.existsByStudentIdAndCourseId(userId, courseId)).thenReturn(false);

        // when + then
        assertThrows(IllegalArgumentException.class, () -> reviewService.create(currentUser, request));

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void create_shouldThrowIfAlreadyReviewed() {
        // given
        Long courseId = 1L;
        Long userId = 7L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(userId);

        ReviewCreateRequest request = new ReviewCreateRequest();
        request.setCourseId(courseId);
        request.setRating(5);
        request.setText("Text");

        Course course = new Course();
        course.setId(courseId);

        User author = new User();
        author.setId(userId);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(userRepository.findById(userId)).thenReturn(Optional.of(author));

        when(enrollmentRepository.existsByStudentIdAndCourseId(userId, courseId)).thenReturn(true);
        when(reviewRepository.existsByAuthorIdAndCourseId(userId, courseId)).thenReturn(true);

        // when + then
        assertThrows(IllegalArgumentException.class, () -> reviewService.create(currentUser, request));

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void getByCourse_shouldReturnList() {
        // given
        Long courseId = 1L;

        Review r1 = new Review();
        Review r2 = new Review();

        when(reviewRepository.findAllByCourseId(courseId)).thenReturn(List.of(r1, r2));

        ReviewResponse m1 = mock(ReviewResponse.class);
        ReviewResponse m2 = mock(ReviewResponse.class);

        when(reviewMapper.toResponse(r1)).thenReturn(m1);
        when(reviewMapper.toResponse(r2)).thenReturn(m2);

        // when
        List<ReviewResponse> result = reviewService.getByCourse(courseId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(m1, result.get(0));
        assertSame(m2, result.get(1));
    }

    @Test
    void update_shouldThrowIfNotAuthor() {
        // given
        Long reviewId = 10L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(99L);

        User author = new User();
        author.setId(7L);

        Review review = new Review();
        review.setId(reviewId);
        review.setAuthor(author);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        ReviewUpdateRequest request = new ReviewUpdateRequest();
        request.setRating(4);
        request.setText("Updated text");

        // when + then
        assertThrows(IllegalArgumentException.class, () -> reviewService.update(currentUser, reviewId, request));

        verify(reviewRepository, never()).save(any());
    }

    @Test
    void delete_shouldThrowIfNotAuthor() {
        // given
        Long reviewId = 10L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(99L);

        User author = new User();
        author.setId(7L);

        Review review = new Review();
        review.setId(reviewId);
        review.setAuthor(author);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // when + then
        assertThrows(IllegalArgumentException.class, () -> reviewService.delete(currentUser, reviewId));

        verify(reviewRepository, never()).delete(any());
    }

    @Test
    void delete_shouldDeleteIfAuthor() {
        // given
        Long reviewId = 10L;
        Long userId = 7L;

        CustomUserDetails currentUser = mock(CustomUserDetails.class);
        when(currentUser.getId()).thenReturn(userId);

        User author = new User();
        author.setId(userId);

        Review review = new Review();
        review.setId(reviewId);
        review.setAuthor(author);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // when
        reviewService.delete(currentUser, reviewId);

        // then
        verify(reviewRepository).delete(review);
    }
}
