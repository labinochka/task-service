package ru.effectivemobile.taskservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.ShortTaskResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;
import ru.effectivemobile.taskservice.entity.TaskEntity;

import java.util.List;

@Mapper(uses = CommentMapper.class)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    TaskEntity toEntity(TaskRequest request);

    TaskResponse toResponse(TaskEntity entity);

    List<ShortTaskResponse> toResponse (List<TaskEntity> entities);
}
