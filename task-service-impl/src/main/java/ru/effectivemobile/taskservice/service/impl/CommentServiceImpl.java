package ru.effectivemobile.taskservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(UUID taskId, CommentRequest request, CustomUserDetails user) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        if (user.getId().equals(task.getExecutorId()) || user.getRole().equals(Role.ADMIN)) {
            CommentEntity comment = commentMapper.toEntity(request);
            comment.setTask(task);

            return commentMapper.toResponse(
                    commentRepository.save(comment)
            );
        }
        throw new AccessForbiddenException();
    }

    @Override
    public CommentResponse updateComment(UUID id, CommentRequest request, CustomUserDetails user) {
        CommentEntity oldComment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        if (user.getId().equals(oldComment.getAuthorId())) {
            CommentEntity newComment = commentMapper.toEntity(request);
            newComment.setId(oldComment.getId());
            newComment.setTask(oldComment.getTask());

            return commentMapper.toResponse(
                    commentRepository.save(newComment)
            );
        }
        throw new AccessForbiddenException();
    }

    @Override
    public void deleteComment(UUID id, CustomUserDetails user) {
        CommentEntity oldComment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        if (user.getId().equals(oldComment.getAuthorId()) || user.getRole().equals(Role.ADMIN)) {
            commentRepository.delete(oldComment);
        }
        throw new AccessForbiddenException();
    }
}
