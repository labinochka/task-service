package ru.effectivemobile.taskservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskservice.api.TaskApi;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;

import java.util.UUID;

@RestController
public class TaskController implements TaskApi {

    @Override
    public TaskResponse create(TaskRequest request) {
        return null;
    }

    @Override
    public TaskResponse getById(UUID id) {
        return null;
    }

    @Override
    public Page<TaskResponse> getAll(Status status, Priority priority, UUID authorId, UUID executorId, String search,
                                     boolean isEarlyFirst, int page, int size) {
        return null;
    }

    @Override
    public TaskResponse update(UUID id, TaskRequest request) {
        return null;
    }

    @Override
    public CommentResponse createComment(UUID taskId, CommentRequest request) {
        return null;
    }

    @Override
    public CommentResponse updateComment(UUID id, CommentRequest request) {
        return null;
    }
}
