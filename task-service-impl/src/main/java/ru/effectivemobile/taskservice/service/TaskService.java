package ru.effectivemobile.taskservice.service;

import org.springframework.data.domain.Page;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.ShortTaskResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponse create(TaskRequest request);

    TaskResponse getById(UUID id, int commentPage, int commentSize);

    Page<ShortTaskResponse> getAll(List<Status> status, List<Priority> priority, List<UUID> authorId, List<UUID> executorId,
                                   String search, boolean isEarlyFirst, int page, int size);

    TaskResponse update(UUID id, TaskRequest request);

    void delete(UUID id);

    TaskResponse updateStatus(UUID taskId, Status status, CustomUserDetails user);
}
