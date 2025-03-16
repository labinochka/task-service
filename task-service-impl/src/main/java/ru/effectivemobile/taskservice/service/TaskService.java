package ru.effectivemobile.taskservice.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    TaskResponse create(TaskRequest request);

    TaskResponse getById(UUID id);

    Page<TaskResponse> getAll(List<Status> status, List<Priority> priority, List<UUID> authorId, List<UUID> executorId,
                              String search, boolean isEarlyFirst, int page, int size);

    TaskResponse update(UUID id, TaskRequest request);

    void delete(UUID id);
}
