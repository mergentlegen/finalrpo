package kz.soft.finalrpo.mapper;

import kz.soft.finalrpo.dto.user.UserProfileResponse;
import kz.soft.finalrpo.dto.user.UserResponse;
import kz.soft.finalrpo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "role.name")
    UserResponse toResponse(User user);

    @Mapping(target = "role", source = "role.name")
    UserProfileResponse toProfileResponse(User user);
}

