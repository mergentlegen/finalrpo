package kz.soft.finalrpo.service;

import kz.soft.finalrpo.dto.lesson.LessonCreateRequest;
import kz.soft.finalrpo.dto.lesson.LessonUpdateRequest;
import kz.soft.finalrpo.dto.lesson.LessonResponse;
import kz.soft.finalrpo.entity.Course;
import kz.soft.finalrpo.entity.Lesson;
import kz.soft.finalrpo.mapper.LessonMapper;
import kz.soft.finalrpo.repository.CourseRepository;
import kz.soft.finalrpo.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    public LessonResponse create(LessonCreateRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Lesson lesson = Lesson.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .course(course)
                .build();

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    public List<LessonResponse> getByCourse(Long courseId) {
        return lessonRepository.findAllByCourseId(courseId)
                .stream().map(lessonMapper::toResponse).toList();
    }

    public LessonResponse update(Long lessonId, LessonUpdateRequest request) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());

        return lessonMapper.toResponse(lessonRepository.save(lesson));
    }

    public void delete(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }
}
