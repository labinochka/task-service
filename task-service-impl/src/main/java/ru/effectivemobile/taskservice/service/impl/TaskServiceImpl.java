package ru.effectivemobile.taskservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Role;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.ShortTaskResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;
import ru.effectivemobile.taskservice.entity.TaskEntity;
import ru.effectivemobile.taskservice.exception.model.AccessForbiddenException;
import ru.effectivemobile.taskservice.exception.model.TaskNotFoundException;
import ru.effectivemobile.taskservice.mapper.TaskMapper;
import ru.effectivemobile.taskservice.repository.TaskRepository;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;
import ru.effectivemobile.taskservice.service.TaskService;
import ru.effectivemobile.taskservice.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final UserService userService;

    @Override
    @Transactional
    public TaskResponse create(TaskRequest request) {
        TaskEntity task = taskMapper.toEntity(request);
        task.setAuthorId(userService.getCurrentUserId());

        TaskEntity savedTask = taskRepository.saveAndFlush(task);

        return taskMapper.toResponse(savedTask);

    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getById(UUID id) {
        return taskMapper.toResponse(
                taskRepository.findById(id)
                        .orElseThrow(() -> new TaskNotFoundException(id))
        );
    }

    @Override
    public Page<ShortTaskResponse> getAll(List<Status> status, List<Priority> priority, List<UUID> authorId,
                                          List<UUID> executorId, String search, boolean isEarlyFirst, int page, int size) {
        List<TaskEntity> result;

        if (isEarlyFirst) {
            result = taskRepository.findAllByFilters(status, priority, authorId, executorId, search).stream()
                            .sorted(Comparator.comparing(TaskEntity::getCreatedAt)).toList();
        } else {
            result = taskRepository.findAllByFilters(status, priority, authorId, executorId, search).stream()
                    .sorted(Comparator.comparing(TaskEntity::getCreatedAt).reversed()).toList();
        }

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), result.size());

        List<ShortTaskResponse> paginatedTasks = taskMapper.toResponse(result.subList(start, end));

        return new PageImpl<>(paginatedTasks, pageable, result.size());
    }

    @Override
    @Transactional
    public TaskResponse update(UUID id, TaskRequest request) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setExecutorId(request.executorId());

        return taskMapper.toResponse(
                taskRepository.save(task)
        );
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    @Override
    @Transactional
    public TaskResponse updateStatus(UUID taskId, Status status, CustomUserDetails user) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (user.getId().equals(task.getExecutorId()) || user.getRole().equals(Role.ADMIN)) {
            task.setStatus(status);
            return taskMapper.toResponse(
                    taskRepository.save(task)
            );
        }
        throw new AccessForbiddenException();
    }
}
