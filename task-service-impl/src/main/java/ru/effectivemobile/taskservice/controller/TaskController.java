package ru.effectivemobile.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import ru.effectivemobile.taskservice.api.TaskApi;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.dto.response.ShortTaskResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;
import ru.effectivemobile.taskservice.service.CommentService;
import ru.effectivemobile.taskservice.service.TaskService;
import ru.effectivemobile.taskservice.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController implements TaskApi {

    private final TaskService taskService;
    private final CommentService commentService;
    private final UserService userService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse create(TaskRequest request) {
        return taskService.create(request);
    }

    @Override
    public TaskResponse getById(UUID id) {
        return taskService.getById(id);
    }

    @Override
    public Page<ShortTaskResponse> getAll(List<Status> status, List<Priority> priority, List<UUID> authorId,
                                          List<UUID> executorId, String search, boolean isEarlyFirst, int page, int size) {
        return taskService.getAll(status, priority, authorId, executorId, search, isEarlyFirst, page, size);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TaskResponse update(UUID id, TaskRequest request) {
        return taskService.update(id, request);
    }

    @Override
    public TaskResponse updateStatus(UUID taskId, Status status) {
        return taskService.updateStatus(taskId, status, userService.getCurrentUser());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        taskService.delete(id);
    }

    @Override
    public CommentResponse createComment(UUID taskId, CommentRequest request) {
        return commentService.createComment(taskId, request, userService.getCurrentUser());
    }

    @Override
    public CommentResponse updateComment(UUID id, CommentRequest request) {
        return commentService.updateComment(id, request, userService.getCurrentUser());
    }

    @Override
    public void deleteComment(UUID id) {
        commentService.deleteComment(id, userService.getCurrentUser());
    }
}
