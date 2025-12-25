package kz.soft.finalrpo.mapper;

import kz.soft.finalrpo.dto.lesson.LessonResponse;
import kz.soft.finalrpo.entity.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(target = "courseId", source = "course.id")
    LessonResponse toResponse(Lesson lesson);
}
