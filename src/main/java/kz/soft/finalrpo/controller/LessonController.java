package kz.soft.finalrpo.controller;

import jakarta.validation.Valid;
import kz.soft.finalrpo.dto.lesson.LessonCreateRequest;
import kz.soft.finalrpo.dto.lesson.LessonResponse;
import kz.soft.finalrpo.dto.lesson.LessonUpdateRequest;
import kz.soft.finalrpo.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")
    @PostMapping
    public LessonResponse create(@RequestBody @Valid LessonCreateRequest request) {
        return lessonService.create(request);
    }

    @GetMapping("/course/{courseId}")
    public List<LessonResponse> getByCourse(@PathVariable Long courseId) {
        return lessonService.getByCourse(courseId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")
    @PutMapping("/{lessonId}")
    public LessonResponse update(@PathVariable Long lessonId, @RequestBody @Valid LessonUpdateRequest request) {
        return lessonService.update(lessonId, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")
    @DeleteMapping("/{lessonId}")
    public String delete(@PathVariable Long lessonId) {
        lessonService.delete(lessonId);
        return "Lesson deleted!";
    }
}
