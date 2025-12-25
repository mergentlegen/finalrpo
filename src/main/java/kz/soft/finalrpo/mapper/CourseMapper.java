package kz.soft.finalrpo.mapper;

import kz.soft.finalrpo.dto.course.CourseResponse;
import kz.soft.finalrpo.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "teacherEmail", source = "teacher.email")
    CourseResponse toResponse(Course course);
}
