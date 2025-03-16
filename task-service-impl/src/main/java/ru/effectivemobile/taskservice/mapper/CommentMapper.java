package ru.effectivemobile.taskservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.entity.CommentEntity;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    CommentEntity toEntity(CommentRequest request);

    CommentResponse toResponse(CommentEntity entity);

    List<CommentResponse> toResponse(List<CommentEntity> entities);
}
