package kz.soft.finalrpo.mapper;

import kz.soft.finalrpo.dto.enrollment.EnrollmentResponse;
import kz.soft.finalrpo.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "courseTitle", source = "course.title")
    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentEmail", source = "student.email")
    EnrollmentResponse toResponse(Enrollment enrollment);
}
