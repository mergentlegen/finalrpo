package kz.soft.finalrpo.controller;

import jakarta.validation.Valid;
import kz.soft.finalrpo.dto.course.CourseCreateRequest;
import kz.soft.finalrpo.dto.course.CourseResponse;
import kz.soft.finalrpo.dto.course.CourseUpdateRequest;
import kz.soft.finalrpo.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")
    @PostMapping
    public CourseResponse create(@RequestBody @Valid CourseCreateRequest request) {
        return courseService.create(request);
    }

    @GetMapping
    public List<CourseResponse> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public CourseResponse getById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")
    @PutMapping("/{id}")
    public CourseResponse update(@PathVariable Long id, @RequestBody @Valid CourseUpdateRequest request) {
        return courseService.update(id, request);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        courseService.delete(id);
        return "Course deleted!";
    }
}
