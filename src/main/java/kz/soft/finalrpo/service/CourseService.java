package kz.soft.finalrpo.service;

import kz.soft.finalrpo.dto.course.CourseCreateRequest;
import kz.soft.finalrpo.dto.course.CourseUpdateRequest;
import kz.soft.finalrpo.dto.course.CourseResponse;
import kz.soft.finalrpo.entity.Course;
import kz.soft.finalrpo.entity.User;
import kz.soft.finalrpo.mapper.CourseMapper;
import kz.soft.finalrpo.repository.CourseRepository;
import kz.soft.finalrpo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    public CourseResponse create(CourseCreateRequest request) {
        User teacher = userRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .build();

        return courseMapper.toResponse(courseRepository.save(course));
    }

    public List<CourseResponse> getAll() {
        return courseRepository.findAll().stream().map(courseMapper::toResponse).toList();
    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return courseMapper.toResponse(course);
    }

    public CourseResponse update(Long id, CourseUpdateRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());

        return courseMapper.toResponse(courseRepository.save(course));
    }

    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
