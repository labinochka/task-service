package ru.effectivemobile.taskservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskservice.dto.enumeration.Priority;
import ru.effectivemobile.taskservice.dto.enumeration.Role;
import ru.effectivemobile.taskservice.dto.enumeration.Status;
import ru.effectivemobile.taskservice.dto.request.TaskRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.dto.response.ShortTaskResponse;
import ru.effectivemobile.taskservice.dto.response.TaskResponse;
import ru.effectivemobile.taskservice.entity.CommentEntity;
import ru.effectivemobile.taskservice.entity.TaskEntity;
import ru.effectivemobile.taskservice.entity.UserEntity;
import ru.effectivemobile.taskservice.exception.model.AccessForbiddenException;
import ru.effectivemobile.taskservice.exception.model.TaskNotFoundException;
import ru.effectivemobile.taskservice.mapper.CommentMapper;
import ru.effectivemobile.taskservice.mapper.TaskMapper;
import ru.effectivemobile.taskservice.repository.TaskRepository;
import ru.effectivemobile.taskservice.repository.UserRepository;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;
import ru.effectivemobile.taskservice.service.TaskService;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    @Override
    public TaskResponse create(TaskRequest request) {
        return taskMapper.toResponse(
                taskRepository.save(taskMapper.toEntity(request))
        );
    }

    @Override
    public TaskResponse getById(UUID id, int commentPage, int commentSize) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        TaskResponse taskResponse = taskMapper.toResponse(task);

        Pageable pageable = PageRequest.of(commentPage, commentSize);
        List<CommentEntity> comments = task.getComments();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), comments.size());
        List<CommentResponse> commentResponses = commentMapper.toResponse(comments.subList(start, end));

        taskResponse.setComments(new PageImpl<>(commentResponses, pageable, comments.size()));
        return taskResponse;
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
    public TaskResponse update(UUID id, TaskRequest request) {
        TaskEntity oldTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        TaskEntity newTask = taskMapper.toEntity(request);
        newTask.setId(oldTask.getId());
        newTask.setComments(oldTask.getComments());
        newTask.setCreatedAt(oldTask.getCreatedAt());

        TaskEntity task = taskRepository.save(newTask);

        return taskMapper.toResponse(task);
    }

    @Override
    public void delete(UUID id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    @Override
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
