package ru.effectivemobile.taskservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.effectivemobile.taskservice.dto.enumeration.Role;
import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.entity.CommentEntity;
import ru.effectivemobile.taskservice.entity.TaskEntity;
import ru.effectivemobile.taskservice.exception.model.AccessForbiddenException;
import ru.effectivemobile.taskservice.exception.model.CommentNotFoundException;
import ru.effectivemobile.taskservice.exception.model.TaskNotFoundException;
import ru.effectivemobile.taskservice.mapper.CommentMapper;
import ru.effectivemobile.taskservice.repository.CommentRepository;
import ru.effectivemobile.taskservice.repository.TaskRepository;
import ru.effectivemobile.taskservice.security.userdetails.CustomUserDetails;
import ru.effectivemobile.taskservice.service.CommentService;
import ru.effectivemobile.taskservice.service.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    private final CommentMapper commentMapper;

    private final UserService userService;

    @Override
    @Transactional
    public CommentResponse createComment(UUID taskId, CommentRequest request, CustomUserDetails user) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (user.getId().equals(task.getExecutorId()) || user.getRole().equals(Role.ADMIN)) {
            CommentEntity comment = commentMapper.toEntity(request);
            comment.setTask(task);
            comment.setAuthorId(userService.getCurrentUserId());

            return commentMapper.toResponse(
                    commentRepository.saveAndFlush(comment)
            );
        }
        throw new AccessForbiddenException();
    }

    @Override
    @Transactional
    public CommentResponse updateComment(UUID id, CommentRequest request, CustomUserDetails user) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        if (user.getId().equals(comment.getAuthorId())) {
            comment.setComment(request.comment());

            return commentMapper.toResponse(
                    commentRepository.save(comment)
            );
        }
        throw new AccessForbiddenException();
    }

    @Override
    @Transactional
    public void deleteComment(UUID id, CustomUserDetails user) {
        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        if (user.getId().equals(comment.getAuthorId()) || user.getRole().equals(Role.ADMIN)) {
            commentRepository.delete(comment);
        } else {
            throw new AccessForbiddenException();
        }
    }
}
