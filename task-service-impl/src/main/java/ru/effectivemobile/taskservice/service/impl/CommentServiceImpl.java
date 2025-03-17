package ru.effectivemobile.taskservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;
import ru.effectivemobile.taskservice.entity.CommentEntity;
import ru.effectivemobile.taskservice.entity.TaskEntity;
import ru.effectivemobile.taskservice.exception.model.CommentNotFoundException;
import ru.effectivemobile.taskservice.exception.model.TaskNotFoundException;
import ru.effectivemobile.taskservice.mapper.CommentMapper;
import ru.effectivemobile.taskservice.repository.CommentRepository;
import ru.effectivemobile.taskservice.repository.TaskRepository;
import ru.effectivemobile.taskservice.service.CommentService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    private final CommentMapper commentMapper;

    @Override
    public CommentResponse createComment(UUID taskId, CommentRequest request) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        CommentEntity comment = commentMapper.toEntity(request);
        comment.setTask(task);

        return commentMapper.toResponse(
                commentRepository.save(comment)
        );
    }

    @Override
    public CommentResponse updateComment(UUID id, CommentRequest request) {
        CommentEntity oldComment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        CommentEntity newComment = commentMapper.toEntity(request);
        newComment.setId(oldComment.getId());
        newComment.setTask(oldComment.getTask());

        return commentMapper.toResponse(
                commentRepository.save(newComment)
        );
    }

    @Override
    public void deleteComment(UUID id) {
        CommentEntity oldComment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        commentRepository.delete(oldComment);
    }
}
