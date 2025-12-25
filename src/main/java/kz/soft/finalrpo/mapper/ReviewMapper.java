package kz.soft.finalrpo.mapper;

import kz.soft.finalrpo.dto.review.ReviewResponse;
import kz.soft.finalrpo.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorEmail", source = "author.email")
    ReviewResponse toResponse(Review review);
}
