package ru.effectivemobile.taskservice.service;

import ru.effectivemobile.taskservice.dto.request.CommentRequest;
import ru.effectivemobile.taskservice.dto.response.CommentResponse;

import java.util.UUID;

public interface CommentService {

    CommentResponse createComment(UUID taskId, CommentRequest request);

    CommentResponse updateComment(UUID id, CommentRequest request);

    void deleteComment(UUID id);
}
