package ru.effectivemobile.taskservice.mapper;

import org.mapstruct.Mapper;
import ru.effectivemobile.taskservice.dto.request.AuthRequest;
import ru.effectivemobile.taskservice.dto.response.AuthResponse;
import ru.effectivemobile.taskservice.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserEntity toEntity(AuthRequest request);

    AuthResponse toResponse(UserEntity entity);
}
